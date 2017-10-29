Ext.define("App.controller.vasilhame.entregar.EntregarCon", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregar.consulta.Grid",
        "vasilhame.entregar.consulta.Window"
    ],

    init: function() {
        this.control ({
            "entregarConWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregarConGrid": {
                itemdblclick: this.itemDuploClick
            },
            "entregarConGrid > consultasTopBar button[name=btnConfirmar]": {
                click: this.botaoTopBarConfirmar
            },
            "entregarConGrid > consultasTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregarConGrid > consultasTopBar button[name=btnAlterar]": {
                click: this.botaoTopBarAlterar
            },
            "entregarConGrid > consultasTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir
            },
            "entregarConGrid > consultasTopBar button[name=btnFiltroPadrao]": {
                click: this.botaoTopBarFiltroPadrao
            },
            "entregarConGrid > consultasTopBar button[name=btnFiltroAvancado]": {
                click: this.botaoTopBarFiltrosAvancados
            },
            "entregarConGrid > consultasTopBar button[name=btnLimpar]": {
                click: this.botaoTopBarLimpar
            },
            "entregarConGrid > consultasTopBar button[name=btnOrdenar]": {
                click: this.botaoTopBarOrdenar
            },
            "entregarConGrid > consultasTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregarConGrid > consultasTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            }
        });
    },

    onReady: function(window) {
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.forceFocus = getFocusOrder(window.down("grid"));
        focusFirstOrForce.focus();
    },
    
    onExit: function(window) {
        if (window.params) {
            if (window.params.destinos) {
                if (window.params.destinos.focus) {
                    // Chamada para a função de foco
                    var focusFirstOrForce = new FocusFirstOrForce();
                    focusFirstOrForce.forceFocus = window.params.destinos.focus;
                    focusFirstOrForce.focusDelay = FOCUSDELAYCLOSE;
                    focusFirstOrForce.focus();
                }
            }
        }
    },
   
    botaoTopBarConfirmar: function(button) {
        var window = button.up("window");
        var grid = button.up("entregarConGrid");
        var destinos = grid.params.destinos;
        if (destinos) {
            var records = grid.getSelectionModel().getSelection();
            if (records.length < 1) {
                // Chamada para a função de mensagens ao usuário    
                var callMessageBox = new CallMessageBox();
                callMessageBox.title = "Aviso";
                callMessageBox.msg = "Selecione registros para retornar";
                callMessageBox.icon = Ext.MessageBox.WARNING;
                callMessageBox.buttons = Ext.MessageBox.OK;
                callMessageBox.show();
                return false;
            } else {             
                var idEmpresas = new Array();
                var idFiliais = new Array();
                var ids = new Array();      
                
                for (var i=0; i<records.length; i++) {
                    idEmpresas[i] = records[i].data.idEmpresa;
                    idFiliais[i] = records[i].data.idFilial;
                    ids[i] = records[i].data.idCodigo;
                }
                for (var i=0; i<destinos.campos.length; i++) {
                    if (destinos.campos[i].tabela == "idEmpresa") {
                        destinos.campos[i].form.setValue(idEmpresas.toString());
                    }
                    if (destinos.campos[i].tabela == "idFilial") {
                        destinos.campos[i].form.setValue(idFiliais.toString());
                    }
                    if (destinos.campos[i].tabela == "idCodigo") {
                        destinos.campos[i].form.setValue(ids.toString());
                    }
                }
                var funcao = destinos.funcao.funcao;
                var parametros = destinos.funcao.parametros;
                destinos.funcao.controller[funcao](parametros[0]);
            }
        }
        window.close();
    },

    botaoTopBarNovo: function(button) {            
        var me = this;
        var grid = button.up("entregarConGrid");
        me.chamaProgramaManutencao(grid);
    },

    botaoTopBarAlterar: function(button) {
        var me = this;
        var grid = button.up("entregarConGrid");        
        var record = grid.getSelectionModel().getSelection();
        if (record.length > 1) {
            // Chamada para a função de mensagens ao usuário    
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Aviso";
            callMessageBox.msg = "Selecione apenas um registro para alterar";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        } 
        else if (record.length < 1) {
            // Chamada para a função de mensagens ao usuário    
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Aviso";
            callMessageBox.msg = "Selecione um registro para alterar";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        } 
        else {
            me.chamaProgramaManutencao(grid, record[0]);
        }
    },

    botaoTopBarExcluir: function(button) {
        var me = this;
        var grid = button.up("entregarConGrid");        
        // Chamada para a função de checagem de permissões do usuário
        var checkPermissions = new CheckPermissions();
        checkPermissions.data = grid.params.permissoes;
        checkPermissions.action = "exclusao";
        if (!checkPermissions.check()) {
            return false;
        }
        var records = grid.getSelectionModel().getSelection();
        if (records.length == 0) {
            // Chamada para a função de mensagens ao usuário    
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Aviso";
            callMessageBox.msg = "Selecione registros para excluir";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        } 
        else {
            // Chamada para a função de mensagens ao usuário    
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Exclusão";
            callMessageBox.msg = "Deseja realmente excluir?";
            callMessageBox.defaultFocus = "no";
            callMessageBox.icon = Ext.MessageBox.QUESTION;
            callMessageBox.buttons = Ext.MessageBox.YESNO;
            callMessageBox.show({
                afterClickBtnYES: function(btn) {
                    var registros = new Array();
                    for (var i=0; i<records.length; i++) {
                        var id = {
                            idEmpresa: records[i].data.idEmpresa,
                            idFilial: records[i].data.idFilial,
                            idCodigo: records[i].data.idCodigo                             
                        };

                        registros.push({
                            id: id,
                            idEmpresa: records[i].data.idEmpresa,
                            idFilial: records[i].data.idFilial,
                            idCodigo: records[i].data.idCodigo
                        });
                    }
                    me.excluirRegistro(grid, registros);
                }
            });  
        }
    },

    botaoTopBarFiltroPadrao: function(button) {  
        callFilterPad({"filterPad": false});
    },

    botaoTopBarFiltrosAvancados: function(button) {  
        callFilterAva({"me": this, "grid": button.up("grid")});
    },

    botaoTopBarLimpar: function(button) {
        var grid = button.up("entregarConGrid");
        grid.headerFilterPlugin.reloadGrid = false;
        grid.clearHeaderFilters();
        grid.headerFilterPlugin.reloadGrid = true;
        grid.filterAva = [];
        grid.params.fieldsPad = [];
        grid.params.fieldsPadValues = {};
        grid.store.currentPage = 1;
        grid.store.load();
    },

    botaoTopBarOrdenar: function(button) {      
        callOrder({"me": this, "grid": button.up("grid")});
    },

    botaoTopBarAjuda: function(button) {
        callHelp(button.up("grid").params.programa);
    },

    botaoTopBarFechar: function(button) {
        var window = button.up("window");
        window.close();
    },
    
    itemDuploClick: function(view, record) {
        callItemDoubleClick({"me": this, "grid": view.up("grid"), "record": record});
    },
    
    chamaProgramaManutencao: function(grid, record) {
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregarCad";
        callProgram.idGridOrigem = grid.id;
        callProgram.store = grid.getStore();
        if (record) {
            callProgram.chave = {
                idEmpresa: record.data.idEmpresa,
                idFilial: record.data.idFilial,
                idCodigo: record.data.idCodigo
            };
        }  
        callProgram.fieldsFix = grid.params.fieldsFix;
        callProgram.call();
    },

    excluirRegistro: function(grid, registros) {
        var request = {
            async: true,
            showMsgErrors: true,
            url: BACKEND + "/vasilhame/entregar/consulta/delete",
            parametros: Ext.JSON.encode({
                registros: registros
            }),
            component: grid,
            waitMsg: DESTROYMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                grid.store.currentPage = 1;
                grid.store.load();
            }
        };
        
        requestServerJson(request);
    },
    
    filtrarGrid: function(grid) {
        grid.store.currentPage = 1;
        grid.store.load();
    },

    ordenarGrid: function(grid, store) {
        ordenarGrid(grid, store);
    }

});