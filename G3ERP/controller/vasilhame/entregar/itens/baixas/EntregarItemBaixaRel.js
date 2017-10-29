Ext.define("App.controller.vasilhame.entregar.itens.baixas.EntregarItemBaixaRel", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregar.itens.baixas.relatorio.Window",
        "vasilhame.entregar.itens.baixas.relatorio.Form"
    ],
    
    init: function() {
        this.control ({
            "entregarItemBaixaRelWindow": {
                show: this.onReady
            },
            
            "entregarItemBaixaRelForm > relatoriosTopBar button[name=btnConfirmar]": {
                click: this.botaoTopBarConfirmar
            },
            "entregarItemBaixaRelForm > relatoriosTopBar button[name=btnOrdenar]": {
                click: this.botaoTopBarOrdenar
            },
            "entregarItemBaixaRelForm > relatoriosTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregarItemBaixaRelForm > relatoriosTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregarItemBaixaRelForm textfield[name=idEmpresa]": {
                focus: this.onFocus,
                blur: this.empresaBlur
            },
            "entregarItemBaixaRelForm button[name=conEmpresas]": {
                click: this.conEmpresasClick
            },
            "entregarItemBaixaRelForm textfield[name=idFilial]": {
                focus: this.onFocus,
                blur: this.filialBlur
            },
            "entregarItemBaixaRelForm button[name=conFiliais]": {
                click: this.conFiliaisClick
            },
            "entregarItemBaixaRelForm textfield[name=idVasilhameEntregar]": {
                focus: this.onFocus,
                blur: this.vasilhameBlur
            },
            "entregarItemBaixaRelForm button[name=conVasilhames]": {
                click: this.conVasilhamesClick
            },
            "entregarItemBaixaRelForm textfield[name=idPessoa]": {
                focus: this.onFocus,
                blur: this.pessoaBlur
            },
            "entregarItemBaixaRelForm button[name=conPessoas]": {
                click: this.conPessoasClick
            },
            "entregarItemBaixaRelForm textfield[name=idUsuarioEntregar]": {
                focus: this.onFocus,
                blur: this.usuarioEntregarBlur
            },
            "entregarItemBaixaRelForm button[name=conUsuariosEntregar]": {
                click: this.conUsuariosEntregarClick
            },
            "entregarItemBaixaRelForm textfield[name=idProduto]": {
                focus: this.onFocus,
                blur: this.produtoBlur
            },
            "entregarItemBaixaRelForm button[name=conProdutos]": {
                click: this.conProdutosClick
            },
            "entregarItemBaixaRelForm textfield[name=idVasilhameEntregarItem]": {
                focus: this.onFocus,
                blur: this.vasilhameItemBlur
            },
            "entregarItemBaixaRelForm button[name=conVasilhamesItens]": {
                click: this.conVasilhamesItensClick
            },
            "entregarItemBaixaRelForm textfield[name=idUsuario]": {
                focus: this.onFocus,
                blur: this.usuarioBlur,
                keydown: this.usuarioKeyDown
            },
            "entregarItemBaixaRelForm button[name=conUsuarios]": {
                click: this.conUsuariosClick
            }            
        });
    },

    onReady: function(window){
        var form = window.down("entregarItemBaixaRelForm");
        var grid = form.down("grid[name=ordem]");
        
        grid.store.loadData([
            ["idEmpresa", "ASC", "Empresa"],
            ["idFilial", "ASC", "Filial"],
            ["idCodigo", "ASC", "ID"]
        ], false);            
            
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.form = form;
        focusFirstOrForce.focus();
    },
    
    botaoTopBarConfirmar: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.carregaRegistro(form, form.getForm().getValues());
    },
    
    botaoTopBarOrdenar: function(button) {
        var form = button.up("entregarItemBaixaRelForm");
        var grid = form.down("grid[name=ordem]");
        if (!(grid.storeOrdCampos)) {
            grid.storeOrdCampos = Ext.create("App.store.Ordenar");
            grid.storeOrdCampos.dados = [
                ["idVasilhameEntregarItem", "ASC", "ID Vasilhame Item"],
                ["tipoBaixa", "ASC", "Tipo Baixa"],
                ["quantidade", "ASC", "Quantidade"],
                ["dataHoraGravacao", "DESC", "Data/Hora Gravação"],
                ["idUsuario", "ASC", "Usuário"]
            ];
            grid.storeOrdCampos.loadData([
                ["idVasilhameEntregarItem", "ASC", "ID Vasilhame Item"],
                ["tipoBaixa", "ASC", "Tipo Baixa"],
                ["quantidade", "ASC", "Quantidade"],
                ["dataHoraGravacao", "DESC", "Data/Hora Gravação"],
                ["idUsuario", "ASC", "Usuário"]
            ], false);
        }
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "ordenar";
        callProgram.storeOrdCampos = grid.storeOrdCampos;
        callProgram.storeOrdOrdens = grid.store;
        callProgram.call();
    },
    
    botaoTopBarAjuda: function(button) {
        callHelp(button.up("form").params.programa);
    },

    botaoTopBarFechar: function(button) {
        var window = button.up("window");
        window.close();
    },
    
    onFocus: function(field) {
        // Propriedade referente ao preenchimento aut. das consultas dos fields
        // Se o field estiver expandido não faz nada
        if (!field.isExpanded) {
            field.oldValue = field.getValue();
        }
    },
    
    empresaBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaRelForm");
            if (field.getValue() == null || field.getValue() == "" || field.getValue() == "0" || isNaN(field.getValue())) {
                form.getForm().findField("nomeEmpresa").setValue("");
                return false;
            }
            var registro = {
                id: field.getValue()
            };
            me.carregaEmpresa(form, registro);
        }
    },
    
    conEmpresasClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idEmpresa"));
        var campos = new Array();
        campos[0] = {
            tabela: "id",
            form: form.getForm().findField("idEmpresa")
        };
        var funcao = {
            controller: me,
            funcao: "empresaBlur",
            parametros: [form.getForm().findField("idEmpresa")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idEmpresa")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "geempr200_";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    filialBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaRelForm");
            if ( (field.getValue() == null) || (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) ) {
                form.getForm().findField("descricaoFilial").setValue("");
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "" || isNaN(idEmpresa) || idEmpresa == null) {
                idEmpresa = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                codigo: field.getValue()
            };
            me.carregaFilial(form, registro);
        }
    },

    conFiliaisClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idFilial"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigo",
            form: form.getForm().findField("idFilial")
        };
        var funcao = {
            controller: me,
            funcao: "filialBlur",
            parametros: [form.getForm().findField("idFilial")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("codigoFilial")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "gefili200_";
        callProgram.destinos = destinos;
        var idEmpresa = form.getForm().findField("idEmpresa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var nomeEmpresa = form.getForm().findField("nomeEmpresa").getValue();    
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: idEmpresa,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "nomeEmpresa", 
            value: nomeEmpresa,
            readOnly: true,
            filterable: false
        });
        callProgram.call();
    },
    
    vasilhameBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregarItemBaixaRelForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var idFilial = form.getForm().findField("idFilial").getValue();
            if (idFilial == "") {
                idFilial = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: field.getValue()
            };
            me.carregaVasilhame(form, registro);
        }
    },

    conVasilhamesClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idVasilhameEntregar"));
        var campos = new Array();
        campos[0] = {
            tabela: "idCodigo",
            form: form.getForm().findField("idVasilhameEntregar")
        };
        var funcao = {
            controller: me,
            funcao: "vasilhameBlur",
            parametros: [form.getForm().findField("idVasilhameEntregar")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idVasilhameEntregar")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregarCon";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var idFilial = form.getForm().findField("idFilial").getValue();
        if ( (isNaN(idFilial)) || (idFilial == null) || (idFilial == "") ) {
            idFilial = 0;
        }
        
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: idEmpresa,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "nomeEmpresa", 
            value: form.getForm().findField("nomeEmpresa").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idFilial", 
            fieldBtn: "conFiliais", 
            value: idFilial,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "descricaoFilial", 
            value: form.getForm().findField("descricaoFilial").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.call();
    },
    
    pessoaBlur: function (field) {
        if (field.getValue() != field.oldValue) {
            var me = this;            
            var form = field.up("entregarItemBaixaRelForm");
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if ( (isNaN(idEmpresa)) || (idEmpresa == "") || (idEmpresa == null) ) {
                idEmpresa = 0;
            }
            if ((isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("nome").setValue("");
                return false;
            }
            var registro = {
                idEmpresa: idEmpresa,
                idPessoa: getCodigoDigito(field.getValue()).codigo,
                digito: getCodigoDigito(field.getValue()).digito
            };            
            me.carregaPessoa(form, registro);
        }
    },
    
    conPessoasClick: function(button) {        
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idPessoa"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigoDigito",
            form: form.getForm().findField("idPessoa")
        };        
        var funcao = {
            controller: me,
            funcao: "pessoaBlur",
            parametros: [form.getForm().findField("idPessoa")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idPessoa")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "gepess200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        var idEmpresa = form.getForm().findField("idEmpresa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var nomeEmpresa = form.getForm().findField("nomeEmpresa").getValue();    
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: idEmpresa,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "nomeEmpresa", 
            value: nomeEmpresa,
            readOnly: true,
            filterable: false
        });
        callProgram.call();        
    },
    
    usuarioEntregarBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaRelForm");
            if (field.getValue() == "" || field.getValue() == null) {
                form.getForm().findField("nomeUsuarioEntregar").setValue("");
                return false;
            }
            var registro = {
                login: field.getValue()
            };
            me.carregaUsuarioEntregar(form, registro);
        }
    },
    
    conUsuariosEntregarClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idUsuarioEntregar"));
        var campos = new Array();
        campos[0] = {
            tabela: "login",
            form: form.getForm().findField("idUsuarioEntregar")
        };
        var funcao = {
            controller: me,
            funcao: "usuarioEntregarBlur",
            parametros: [form.getForm().findField("idUsuarioEntregar")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idUsuarioEntregar")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "siusua200_";
        callProgram.destinos = destinos;
        callProgram.call();
    },
    
    produtoBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaRelForm");            
            if ((isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("idProduto").setValue("");
                form.getForm().findField("descricaoProduto").setValue("");
                return false;
            }
            
            me.carregaProduto(form);
        }
    },
    
    conProdutosClick: function(button) {        
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idProduto"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigo_digito_esprod",
            form: form.getForm().findField("idProduto")
        };        
        var funcao = {
            controller: me,
            funcao: "produtoBlur",
            parametros: [form.getForm().findField("idProduto")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idProduto")
        };
        
        // Chamada para a função de carregamento dos programas
        var callProgram = new CallProgram();
        callProgram.programa = "esprod200";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },
    
    vasilhameItemBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregarItemBaixaRelForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "" || isNaN(idEmpresa) || idEmpresa == null) {
                idEmpresa = 0;
            }
            
            var idFilial = form.getForm().findField("idFilial").getValue();
            if (idFilial == "" || isNaN(idFilial) || idFilial == null) {            
                idFilial = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: field.getValue()
            };
            me.carregaVasilhameItem(form, registro);
        }
    },

    conVasilhamesItensClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        var campos = new Array();
        campos[0] = {
            tabela: "idCodigo",
            form: form.getForm().findField("idVasilhameEntregarItem")
        };
        var funcao = {
            controller: me,
            funcao: "vasilhameItemBlur",
            parametros: [form.getForm().findField("idVasilhameEntregarItem")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idVasilhameEntregarItem")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregarItemCon";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var idFilial = form.getForm().findField("idFilial").getValue();
        if ( (isNaN(idFilial)) || (idFilial == null) || (idFilial == "") ) {
            idFilial = 0;
        }
        
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: idEmpresa,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "nomeEmpresa", 
            value: form.getForm().findField("nomeEmpresa").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "idFilial", 
            fieldBtn: "conFiliais", 
            value: idFilial,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "descricaoFilial", 
            value: form.getForm().findField("descricaoFilial").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.call();
    },
   
    usuarioBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaRelForm");
            if (field.getValue() == "") {
                form.getForm().findField("nomeUsuario").setValue("");
                return false;
            }
            var registro = {
                login: field.getValue()
            };
            me.carregaUsuario(form, registro);
        }
    },
    
    conUsuariosClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaRelForm");
        me.onFocus(form.getForm().findField("idUsuario"));
        var campos = new Array();
        campos[0] = {
            tabela: "login",
            form: form.getForm().findField("idUsuario")
        };
        var funcao = {
            controller: me,
            funcao: "usuarioBlur",
            parametros: [form.getForm().findField("idUsuario")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idUsuario")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "siusua200_";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    usuarioKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregarItemBaixaRelForm");
                me.botaoTopBarConfirmar(form.down("relatoriosTopBar button[name=btnConfirmar]"));
            }
        }
    },  
    
    carregaRegistro: function(form, registros) {
        var erros = form.getForm().findInvalid();        
        var grid = form.down("grid[name=ordem]");        
        if (erros.length > 0) {
            // Chamada para a função de foco
            var focusFirstOrForce = new FocusFirstOrForce();
            focusFirstOrForce.forceFocus = erros[0];
            focusFirstOrForce.focusDelay = FOCUSDELAYERRO;
            focusFirstOrForce.focus();
        } else {     
            var sort = new Array();
            for (var i in grid.store.data.items) {
                var obj = new Object();
                Ext.Object.each(grid.store.data.items[i].data, function(key, value) {
                    if (key != "descricao" && key != "id") {
                        obj[key] = value;
                    }
                });
                sort.push(obj);
            }            
            
            var url;
            var formato = registros.formato;
            if (formato == "PDF") {
                url = BACKEND + "/vasilhame/entregar/itens/baixas/relatorio/pdf";
            } else if (formato == "TXT") {
                url = BACKEND + "/vasilhame/entregar/itens/baixas/relatorio/txt";
            }      
            
            if (registros.idPessoa) {
                var pessoa = registros.idPessoa.split(",");
                var codPessoa = "";
                for (var i=0; i<pessoa.length; i++) {
                    if (codPessoa !="") {
                        codPessoa += ","
                    }
                    codPessoa += getCodigoDigito(pessoa[i]).codigo;
                }            
                registros.idPessoa = codPessoa;
            }
            
            if (registros.idProduto) {
                var produto = registros.idProduto.split(",");
                var codProduto = "";
                for (var i=0; i<produto.length; i++) {
                    if (codProduto !="") {
                        codProduto += ","
                    }
                    codProduto += getCodigoDigito(produto[i]).codigo;
                }            
                registros.idProduto = codProduto;
            }
            
            var callReport = new CallReport1();
            callReport.url = url;
            callReport.format = formato;
            callReport.nameFile = "Baixas dos Itens dos Vasilhames a Entregar." + formato.toLowerCase();
            callReport.records = registros;            
            callReport.sort = sort; 
            callReport.delimiter = RELATORIOSTXTDELIMITADOR;
            callReport.breakLine = RELATORIOSTXTQUEBRALINHA;                 
            callReport.call();
      
        }
    },
    
    carregaEmpresa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/exists/empresa",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idEmpresa"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeEmpresa").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                form.getForm().findField("nomeEmpresa").setValue("");
            }
        };
        
        requestServerJson(request);
    },
    
    carregaFilial: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/exists/filial",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idFilial"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("descricaoFilial").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                form.getForm().findField("descricaoFilial").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaVasilhame: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/cadastro/exists/vasilhame",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idVasilhameEntregar"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
//                form.getForm().findField("descricaoFilial").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idCodigo") {
                            form.getForm().findField("idVasilhameEntregar").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
//                form.getForm().findField("descricaoFilial").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaPessoa: function (form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/cadastro/exists/pessoa",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idPessoa"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function (resposta, request) {
                form.getForm().findField("nome").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function (resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idPessoa" || resposta.errors[i].id == "digitoPessoa") {
                            form.getForm().findField("idPessoa").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("nome").setValue("");
            }
        };

        requestServerJson(request);
    },
    
    carregaUsuarioEntregar: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/cadastro/exists/usuario",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idUsuarioEntregar"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeUsuarioEntregar").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                form.getForm().findField("nomeUsuarioEntregar").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaProduto: function(form) {
        var codprodutos = new Array();
        var digprodutos = new Array();
        var produtos = form.getForm().findField("idProduto").getValue().split(",");
        
        for (var i in produtos) {     
            codprodutos[i] = produtos[i];
            digprodutos[i] = 0;
            var tam = produtos[i].toString().length;
            if (tam > 1) {
                digprodutos[i] = produtos[i].substring(tam-1);
                codprodutos[i] = produtos[i].substring(0, tam-1);
            }      
        }
        var chave = {
            codigo_esprod: codprodutos.toString(),
            digito_esprod: digprodutos.toString()
        };
        var url = "ChamaBD?programa=G3BD.Estoque.esprod100";

        var parametros = "funcao=veriExisteDigitoProdutoJSON&chave=" + Ext.JSON.encode(chave);
        var xmlhttp = iniciaXMLHTTP();
        xmlhttp.open("POST", url, false);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        xmlhttp.send(parametros);
        var resposta = Ext.JSON.decode(xmlhttp.responseText);
        if (resposta.success) {            
            form.getForm().findField("descricaoProduto").setValue(resposta.data.descricao_esprod);
            if (resposta.msg) {
                // Chamada para a função de mensagens ao usuário    
                var callMessageBox = new CallMessageBox();
                callMessageBox.title = "Sucesso";
                callMessageBox.msg = resposta.msg;
                callMessageBox.icon = Ext.MessageBox.INFO;
                callMessageBox.buttons = Ext.MessageBox.OK;
                callMessageBox.show();
            }
        } else {
            if (resposta.errors) {
                for (var i in resposta.errors) {
                    if (resposta.errors[i].id == "digito_esprod") {
                        form.getForm().findField("idProduto").markInvalid(resposta.errors[i].msg);
                    }
                    if (resposta.errors[i].id == "codigo_esprod") {
                        form.getForm().findField("idProduto").markInvalid(resposta.errors[i].msg);
                    }
                }
            }                                
            form.getForm().findField("descricaoProduto").setValue("");
            if (resposta.msg) {
                // Chamada para a função de mensagens ao usuário    
                var callMessageBox = new CallMessageBox();
                callMessageBox.title = "Erro";
                callMessageBox.msg = resposta.msg;
                callMessageBox.icon = Ext.MessageBox.ERROR;
                callMessageBox.buttons = Ext.MessageBox.OK;
                callMessageBox.show();
            }
        }    
    },
    
    carregaVasilhameItem: function(form, registro) {        
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/exists/item",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idVasilhameEntregarItem"),
            waitMsg: LOADMSG
        };
        
        requestServerJson(request);
    },
    
    carregaUsuario: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/exists/usuario",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idUsuario"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeUsuario").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                form.getForm().findField("nomeUsuario").setValue("");
            }           
        };
        
        requestServerJson(request);
    }

});