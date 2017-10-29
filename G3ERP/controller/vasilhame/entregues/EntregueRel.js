Ext.define("App.controller.vasilhame.entregues.EntregueRel", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.relatorio.Window",
        "vasilhame.entregues.relatorio.Form"
    ],
    
    init: function() {
        this.control ({
            "entregueRelWindow": {
                show: this.onReady
            },
            
            "entregueRelForm > relatoriosTopBar button[name=btnConfirmar]": {
                click: this.botaoTopBarConfirmar
            },
            "entregueRelForm > relatoriosTopBar button[name=btnOrdenar]": {
                click: this.botaoTopBarOrdenar
            },
            "entregueRelForm > relatoriosTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueRelForm > relatoriosTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregueRelForm textfield[name=idEmpresa]": {
                focus: this.onFocus,
                blur: this.empresaBlur
            },
            "entregueRelForm button[name=conEmpresas]": {
                click: this.conEmpresasClick
            },
            "entregueRelForm textfield[name=idFilial]": {
                focus: this.onFocus,
                blur: this.filialBlur
            },
            "entregueRelForm button[name=conFiliais]": {
                click: this.conFiliaisClick
            },
            "entregueRelForm textfield[name=idPessoa]": {
                focus: this.onFocus,
                blur: this.pessoaBlur
            },
            "entregueRelForm button[name=conPessoas]": {
                click: this.conPessoasClick
            },
            "entregueRelForm textfield[name=idUsuario]": {
                focus: this.onFocus,
                blur: this.usuarioBlur,
                keydown: this.usuarioKeyDown
            },
            "entregueRelForm button[name=conUsuarios]": {
                click: this.conUsuariosClick
            }            
        });
    },

    onReady: function(window){
        var form = window.down("entregueRelForm");
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
        var form = button.up("entregueRelForm");
        me.carregaRegistro(form, form.getForm().getValues());
    },
    
    botaoTopBarOrdenar: function(button) {
        var form = button.up("entregueRelForm");
        var grid = form.down("grid[name=ordem]");
        if (!(grid.storeOrdCampos)) {
            grid.storeOrdCampos = Ext.create("App.store.Ordenar");
            grid.storeOrdCampos.dados = [
                ["idPessoa", "ASC", "Pessoa"],
                ["nome", "ASC", "Nome Pessoa"],
                ["tipoContribuinte", "ASC", "Contribuinte"],
                ["cpfcnpj", "ASC", "CPF/CNPJ"],
                ["observacao", "ASC", "Observacao"],
                ["situacao", "ASC", "Situação"],
                ["dataHoraGravacao", "DESC", "Data/Hora Gravação"],
                ["idUsuario", "ASC", "Usuário"]
            ];
            grid.storeOrdCampos.loadData([
                ["idPessoa", "ASC", "Pessoa"],
                ["nome", "ASC", "Nome Pessoa"],
                ["tipoContribuinte", "ASC", "Contribuinte"],
                ["cpfcnpj", "ASC", "CPF/CNPJ"],
                ["observacao", "ASC", "Observacao"],
                ["situacao", "ASC", "Situação"],
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
            var form = field.up("entregueRelForm");
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
        var form = button.up("entregueRelForm");
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
            var form = field.up("entregueRelForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || field.getValue() == null) {
                form.getForm().findField("descricaoFilial").setValue("");
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "" || isNaN(idEmpresa)) {
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
        var form = button.up("entregueRelForm");
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
            focus: form.getForm().findField("idFilial")
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
    
    pessoaBlur: function (field) {
        if (field.getValue() != field.oldValue) {
            var me = this;            
            var form = field.up("entregueRelForm");
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if ((isNaN(idEmpresa)) || (idEmpresa == "")) {
                idEmpresa = 0;
            }
            if ((isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null)) {
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
        var form = button.up("entregueRelForm");
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
    
    usuarioBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueRelForm");
            if (field.getValue() == "" || field.getValue() == null) {
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
        var form = button.up("entregueRelForm");
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
                var form = field.up("entregueRelForm");
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
                url = BACKEND + "/vasilhame/entregues/relatorio/pdf";
            } else if (formato == "TXT") {
                url = BACKEND + "/vasilhame/entregues/relatorio/txt";
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
            
            var callReport = new CallReport1();
            callReport.url = url;
            callReport.format = formato;
            callReport.nameFile = "Vasilhames Entregues." + formato.toLowerCase();
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
            url: BACKEND + "/vasilhame/entregues/cadastro/exists/empresa",
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
            url: BACKEND + "/vasilhame/entregues/cadastro/exists/filial",
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
    
    carregaPessoa: function (form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/cadastro/exists/pessoa",
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
    
    carregaUsuario: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/cadastro/exists/usuario",
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