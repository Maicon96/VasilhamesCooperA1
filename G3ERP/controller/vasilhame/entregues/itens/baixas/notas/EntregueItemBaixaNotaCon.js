Ext.define("App.controller.vasilhame.entregues.itens.baixas.notas.EntregueItemBaixaNotaCon", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.itens.baixas.notas.consulta.Grid",
        "vasilhame.entregues.itens.baixas.notas.consulta.Window"
    ],

    init: function() {
        this.control ({
            "entregueItemBaixaNotaConWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregueItemBaixaNotaConGrid": {
                itemdblclick: this.itemDuploClick
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnConfirmar]": {
                click: this.botaoTopBarConfirmar
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnAlterar]": {
                click: this.botaoTopBarAlterar
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnFiltroPadrao]": {
                click: this.botaoTopBarFiltroPadrao
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnFiltroAvancado]": {
                click: this.botaoTopBarFiltrosAvancados
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnLimpar]": {
                click: this.botaoTopBarLimpar
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnOrdenar]": {
                click: this.botaoTopBarOrdenar
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueItemBaixaNotaConGrid > consultasTopBar button[name=btnFechar]": {
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
        var grid = button.up("entregueItemBaixaNotaConGrid");
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
               var idEmpresasBaixas = new Array();
                var idFiliaisBaixas = new Array();
                var idBaixas = new Array();      
                var idEmpresasNotas = new Array();
                var modelos = new Array();
                var idFiliaisNotas = new Array();
                var series = new Array();
                var numeros = new Array();
                var dataEmissoes = new Array();
                var sequencias = new Array();
                
                for (var i=0; i<records.length; i++) {
                    idEmpresasBaixas[i] = records[i].data.idEmpresaBaixa;
                    idFiliaisBaixas[i] = records[i].data.idFilialBaixa;
                    idBaixas[i] = records[i].data.idVasilhameEntregueItemBaixa;
                    idEmpresasNotas = records[i].data.idEmpresaNota;
                    modelos = records[i].data.modelo;
                    idFiliaisNotas = records[i].data.idFilialNota;
                    series = records[i].data.serie;
                    numeros = records[i].data.numero;
                    dataEmissoes = records[i].data.dataEmissao;
                    sequencias = records[i].data.sequencia;
                }
                for (var i=0; i<destinos.campos.length; i++) {
                    if (destinos.campos[i].tabela == "idEmpresaBaixa") {
                        destinos.campos[i].form.setValue(idEmpresasBaixas.toString());
                    }
                    if (destinos.campos[i].tabela == "idFilialBaixa") {
                        destinos.campos[i].form.setValue(idFiliaisBaixas.toString());
                    }
                    if (destinos.campos[i].tabela == "idVasilhameEntregueItemBaixa") {
                        destinos.campos[i].form.setValue(idBaixas.toString());
                    }
                    if (destinos.campos[i].tabela == "idEmpresaNota") {
                        destinos.campos[i].form.setValue(idEmpresasNotas.toString());
                    }
                    if (destinos.campos[i].tabela == "modelo") {
                        destinos.campos[i].form.setValue(modelos.toString());
                    }
                    if (destinos.campos[i].tabela == "idFilialNota") {
                        destinos.campos[i].form.setValue(idFiliaisNotas.toString());
                    }
                    if (destinos.campos[i].tabela == "serie") {
                        destinos.campos[i].form.setValue(series.toString());
                    }
                    if (destinos.campos[i].tabela == "numero") {
                        destinos.campos[i].form.setValue(numeros.toString());
                    }
                    if (destinos.campos[i].tabela == "dataEmissao") {
                        destinos.campos[i].form.setValue(dataEmissoes.toString());
                    }
                    if (destinos.campos[i].tabela == "sequencia") {
                        destinos.campos[i].form.setValue(sequencias.toString());
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
        var grid = button.up("entregueItemBaixaNotaConGrid");
        me.chamaProgramaManutencao(grid);
    },

    botaoTopBarAlterar: function(button) {
        var me = this;
        var grid = button.up("entregueItemBaixaNotaConGrid");        
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
        var grid = button.up("entregueItemBaixaNotaConGrid");        
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
                            idEmpresaBaixa: records[i].data.idEmpresaBaixa,
                            idFilialBaixa: records[i].data.idFilialBaixa,
                            idVasilhameEntregueItemBaixa: records[i].data.idVasilhameEntregueItemBaixa,
                            idEmpresaNota: records[i].data.idEmpresaNota,
                            modelo: records[i].data.modelo,
                            idFilialNota: records[i].data.idFilialNota,
                            serie: records[i].data.serie,
                            numero: records[i].data.numero,
                            dataEmissao: Ext.util.Format.date(records[i].data.dataEmissao, "Y-m-d"),
                            sequencia: records[i].data.sequencia
                        };

                        registros.push({
                            id: id,
                            idEmpresaBaixa: records[i].data.idEmpresaBaixa,
                            idFilialBaixa: records[i].data.idFilialBaixa,
                            idVasilhameEntregueItemBaixa: records[i].data.idVasilhameEntregueItemBaixa,
                            idEmpresaNota: records[i].data.idEmpresaNota,
                            modelo: records[i].data.modelo,
                            idFilialNota: records[i].data.idFilialNota,
                            serie: records[i].data.serie,
                            numero: records[i].data.numero,
                            dataEmissao: Ext.util.Format.date(records[i].data.dataEmissao, "Y-m-d"),
                            sequencia: records[i].data.sequencia
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
        var grid = button.up("entregueItemBaixaNotaConGrid");
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
        callProgram.programa = "vasilhames.EntregueItemBaixaNotaCad";
        callProgram.idGridOrigem = grid.id;
        callProgram.store = grid.getStore();
        if (record) {
            callProgram.chave = {
                idEmpresaBaixa: record.data.idEmpresaBaixa,
                idFilialBaixa: record.data.idFilialBaixa,
                idVasilhameEntregueItemBaixa: record.data.idVasilhameEntregueItemBaixa,
                idEmpresaNota: record.data.idEmpresaNota,
                modelo: record.data.modelo,
                idFilialNota: record.data.idFilialNota,
                serie: record.data.serie,
                numero: record.data.numero,
                dataEmissao: Ext.util.Format.date(record.data.dataEmissao, "Y-m-d"),
                sequencia: record.data.sequencia
            };
        }
        callProgram.fieldsFix = grid.params.fieldsFix;
        callProgram.call();
    },

    excluirRegistro: function(grid, registros) {
        var request = {
            async: true,
            showMsgErrors: true,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/consulta/delete",
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