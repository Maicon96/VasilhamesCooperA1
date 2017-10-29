Ext.define("App.controller.vasilhame.entregar.itens.baixas.EntregarItemBaixaCad", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregar.itens.baixas.cadastro.Window",
        "vasilhame.entregar.itens.baixas.cadastro.Form"
    ],
    
    init: function() {
        this.control ({
            "entregarItemBaixaCadWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregarItemBaixaCadForm > edicoesTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregarItemBaixaCadForm > edicoesTopBar button[name=btnSalvar]": {
                click: this.botaoTopBarSalvar
            },
            "entregarItemBaixaCadForm > edicoesTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir
            },
            "entregarItemBaixaCadForm > edicoesTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregarItemBaixaCadForm > edicoesTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregarItemBaixaCadForm textfield[name=idEmpresa]": {
                focus: this.onFocus,
                blur: this.empresaBlur
            },
            "entregarItemBaixaCadForm button[name=conEmpresas]": {
                click: this.conEmpresasClick
            },
            "entregarItemBaixaCadForm textfield[name=idFilial]": {
                focus: this.onFocus,
                blur: this.filialBlur
            },
            "entregarItemBaixaCadForm button[name=conFiliais]": {
                click: this.conFiliaisClick
            },
            "entregarItemBaixaCadForm textfield[name=idVasilhameEntregarItem]": {
                focus: this.onFocus,
                blur: this.vasilhameItemBlur
            },
            "entregarItemBaixaCadForm button[name=conVasilhamesItens]": {
                click: this.conVasilhamesItensClick
            },
            "entregarItemBaixaCadForm textfield[name=idUsuario]": {
                focus: this.onFocus,
                blur: this.usuarioBlur
            },
            "entregarItemBaixaCadForm button[name=conUsuarios]": {
                click: this.conUsuariosClick
            },
            "entregarItemBaixaCadForm textfield[name=tipoBaixa]": {
                focus: this.onFocus,
                keydown: this.tipoBaixaKeyDown
            }
        });
    },

    onReady: function(window) {        
        var me = this;
        var form = window.down("entregarItemBaixaCadForm");
        
        if (window.params.chave) {
            form.getForm().setValues(window.params.chave);
            me.carregaRegistro(form, window.params.chave, true);
        } else {
            me.botaoTopBarNovo(form.down("edicoesTopBar button[name=btnNovo]"));
        }   
    },
        
    onExit: function(window) {
        if (window.params) {
            if (window.params.idGridOrigem) {
                if (Ext.getCmp(window.params.idGridOrigem)) {
                    if (window.params.store) {
                        window.params.store.currentPage = 1;
                        window.params.store.load();
                    }
                }
            }
        }
    },      
    
    botaoTopBarNovo: function(button){
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
        var ignoreFields = Ext.create("Ext.util.MixedCollection");
        ignoreFields.add("quantidadeEntregar", true);
        ignoreFields.add("quantidadeBaixada", true);        
        me.limparTela(form, ignoreFields);        
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.form = form;
        focusFirstOrForce.focus();
        
        if (form.params) {
            if (!form.params.fieldsFix) {
                form.getForm().findField("idFilial").setValue(USUARIOFIL);
                form.getForm().findField("descricaoFilial").setValue(USUARIOFILDES);
            }            
        }        
        form.getForm().findField("dataHoraGravacao").setValue(new Date);
        form.getForm().findField("idUsuario").setValue(USUARIOLOG);
        form.getForm().findField("nomeUsuario").setValue(USUARIONOM);        
    },
    
    botaoTopBarSalvar: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
        // Chamada para a função de checagem de permissões do usuário
        var checkPermissions = new CheckPermissions();
        checkPermissions.data = form.params.permissoes;
        if (form.down("edicoesTopBar button[name=btnExcluir]").disabled) {
            checkPermissions.action = "inclusao";
            if (!checkPermissions.check()) {
                return false;
            }
        } else {
            checkPermissions.action = "alteracao";
            if (!checkPermissions.check()) {
                return false;
            }
        }
               
        var dados = form.getForm().getValues();
                       
        dados.id = {
            idCodigo: form.getForm().findField("idCodigo").getValue(),
            idEmpresa: form.getForm().findField("idEmpresa").getValue(),
            idFilial: form.getForm().findField("idFilial").getValue()
        };
        
        me.gravaRegistro(form, dados);
    },

    botaoTopBarExcluir: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
        // Chamada para a função de checagem de permissões do usuário
        var checkPermissions = new CheckPermissions();
        checkPermissions.data = form.params.permissoes;
        checkPermissions.action = "exclusao";
        if (!checkPermissions.check()) {
            return false;
        }
        // Chamada para a função de mensagens ao usuário    
        var callMessageBox = new CallMessageBox();
        callMessageBox.title = "Exclusão";
        callMessageBox.msg = "Deseja realmente excluir?";
        callMessageBox.defaultFocus = "no";
        callMessageBox.icon = Ext.MessageBox.QUESTION;
        callMessageBox.buttons = Ext.MessageBox.YESNO;
        callMessageBox.show({
            afterClickBtnYES: function(btn) {
                var id = {
                    idEmpresa: form.getForm().findField("idEmpresa").getValue(),
                    idFilial: form.getForm().findField("idFilial").getValue(),
                    idCodigo: form.getForm().findField("idCodigo").getValue() 
                };
                var registros = [{
                    id: id,
                    idEmpresa: form.getForm().findField("idEmpresa").getValue(),
                    idFilial: form.getForm().findField("idFilial").getValue(),
                    idCodigo: form.getForm().findField("idCodigo").getValue(),
                    idVasilhameEntregarItem: form.getForm().findField("idVasilhameEntregarItem").getValue()
                }];
                me.excluirRegistro(form, registros);
            }
        });         
    },

    botaoTopBarAjuda: function(button) {
        callHelp(button.up("form").params.programa);
    },

    botaoTopBarFechar: function(button) {
        var window = button.up("window");
        window.close();
    },

    onFocus: function(field){
        // Propriedade referente ao preenchimento aut. das consultas dos fields
        // Se o field estiver expandido não faz nada
        if (!field.isExpanded) {
            field.oldValue = field.getValue();
        }
    },
    
    empresaBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregarItemBaixaCadForm");
            var idEmpresa = field.getValue();
            if ( (isNaN(idEmpresa)) || (idEmpresa == "") || (idEmpresa == null) ) {
                idEmpresa = "0";
                form.getForm().findField("nomeEmpresa").setValue("");
            }
            var idFilial = form.getForm().findField("idFilial").getValue();
            if (idFilial == "" || idFilial == null) {
                idFilial = 0;
            }
            var idCodigo = form.getForm().findField("idCodigo").getValue();
            if (idCodigo == "" || idCodigo == null) {
                idCodigo = 0;
            }
            
            form.getForm().findField("idFilial").forceLoad = true;
            var registro = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: idCodigo
            };
            me.carregaRegistro(form, registro);
        }
    },
    
    conEmpresasClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
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
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    filialBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregarItemBaixaCadForm");
            var idFilial = field.getValue();
            if ( (isNaN(idFilial)) || (idFilial == "") || (idFilial == null) ) {
                idFilial = "0";
                form.getForm().findField("descricaoFilial").setValue("");
            }
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "" || idEmpresa == null) {
                idEmpresa = 0;
            }
            var idCodigo = form.getForm().findField("idCodigo").getValue();
            if (idCodigo == "" || idCodigo == null) {
                idCodigo = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: idCodigo
            };
            me.carregaRegistro(form, registro);
        }
    },

    conFiliaisClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
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
    
    vasilhameItemBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregarItemBaixaCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("idProduto").setValue("");
                form.getForm().findField("descricaoProduto").setValue("");
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
            me.carregaVasilhameItem(form, registro);
        }
    },

    conVasilhamesItensClick: function(button) {
        var me = this;
        var form = button.up("entregarItemBaixaCadForm");
        me.onFocus(form.getForm().findField("idVasilhameEntregarItem"));
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
            var form = field.up("entregarItemBaixaCadForm");
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
        var form = button.up("entregarItemBaixaCadForm");
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
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },
    
    tipoBaixaKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregarItemBaixaCadForm");
                if (!form.down("edicoesTopBar button[name=btnSalvar]").isDisabled()) {
                    me.botaoTopBarSalvar(form.down("edicoesTopBar button[name=btnSalvar]"));
                }
            }
        }
    },    
    
    limparTela: function(form, ignoreFields) {
        if (ignoreFields) {
            var fields = form.getForm().getFields();
            for (var i in fields.items) {
                if (!ignoreFields.get(fields.items[i].getName())) {
                    fields.items[i].reset();
                }
            }
        } else {
            form.getForm().reset();
        }
        form.down("edicoesTopBar button[name=btnExcluir]").disable();
        form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Incluir");
        setFieldsFix(form);
    },

    carregaRegistro: function(form, registro, focus) {
        var me = this;        
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/exists",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.limparTela(form);                              
                form.getForm().setValues(resposta.registro);                
                
                var produto = "";
                if (resposta.registro.vasilhameEntregarItem.idProduto) {
                    produto = resposta.registro.vasilhameEntregarItem.idProduto + resposta.registro.vasilhameEntregarItem.produto.digito;
                }
                
                form.getForm().setValues({
                    idEmpresa: resposta.registro.idEmpresa,
                    nomeEmpresa: resposta.registro.empresa.nome,
                    idFilial: resposta.registro.idFilial,
                    descricaoFilial: resposta.registro.filial.descricao,
                    idCodigo: resposta.registro.idCodigo,
                    idVasilhameEntregarItem: resposta.registro.idVasilhameEntregarItem,
                    idProduto: produto,
                    descricaoProduto: resposta.registro.vasilhameEntregarItem.produto.descricao,                    
                    quantidadeEntregar: resposta.registro.vasilhameEntregarItem.quantidade,                    
                    tipoBaixa: resposta.registro.tipoBaixa,
                    quantidade: resposta.registro.quantidade,
                    dataHoraGravacao: resposta.registro.dataHoraGravacao,
                    idUsuario: resposta.registro.idUsuario,
                    nomeUsuario: resposta.registro.usuario.nome
                });
                form.down("edicoesTopBar button[name=btnExcluir]").enable();   
                form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                
                if (focus === true) {
                    // Chamada para a função de foco
                    var focusFirstOrForce = new FocusFirstOrForce();
                    focusFirstOrForce.form = form;
                    focusFirstOrForce.focus();
                }
            },
            afterMsgSuccessFalse: function (resposta, request) {
                var ignoreFields = Ext.create("Ext.util.MixedCollection");
                ignoreFields.add("idEmpresa", true);
                ignoreFields.add("nomeEmpresa", true);
                ignoreFields.add("idFilial", true);
                ignoreFields.add("descricaoFilial", true);
                me.limparTela(form, ignoreFields);
                if (resposta.errors) {
                    form.getForm().markInvalid(resposta.errors);
                }
                
                form.getForm().findField("dataHoraGravacao").setValue(new Date);
                form.getForm().findField("idUsuario").setValue(USUARIOLOG);
                form.getForm().findField("nomeUsuario").setValue(USUARIONOM);
                
                form.down("edicoesTopBar button[name=btnSalvar]").enable();
                
                if (focus === true) {
                    // Chamada para a função de foco
                    var focusFirstOrForce = new FocusFirstOrForce();
                    focusFirstOrForce.form = form;
                    focusFirstOrForce.focus();
                }
            }
        };
        
        requestServerJson(request);
    },

    gravaRegistro: function(form, registro) {
        var me = this;
        var erros = form.getForm().findInvalid();
        if (erros.length > 0) {
            // Chamada para a função de foco
            var focusFirstOrForce = new FocusFirstOrForce();
            focusFirstOrForce.forceFocus = erros[0];
            focusFirstOrForce.focusDelay = FOCUSDELAYERRO;
            focusFirstOrForce.focus();
        } else {     
            var url = "/vasilhame/entregar/itens/baixas/cadastro/save";
            if (registro.idCodigo) {
                url = "/vasilhame/entregar/itens/baixas/cadastro/update";
            }
            
            //verifica se o último conjunto de dados é igual ao que está salvando, se forem iguais não irá salvar
            form.down("edicoesTopBar button[name=btnSalvar]").disable();
            if (form.lastSubmit) {
                if (JSON.stringify(form.lastSubmit) === JSON.stringify(registro)) {
                    form.down("edicoesTopBar button[name=btnSalvar]").enable();
                    // Chamada para a função de foco
                    var focusFirstOrForce = new FocusFirstOrForce();
                    focusFirstOrForce.form = form;
                    focusFirstOrForce.focus();
                    return false;
                }
            }
            
            var request = {
                async: true,
                showMsgErrors: true,
                url: BACKEND + url,
                parametros: Ext.JSON.encode({
                    registro: registro
                }),
                component: form,
                btnSalvar: form.down("edicoesTopBar button[name=btnSalvar]"),
                waitMsg: SAVEMSG,
                beforeMsgSuccessTrue: function(resposta, request) {
                    form.getForm().findField("idCodigo").setValue(resposta.registro.id.idCodigo);    
                    form.getForm().findField("quantidadeBaixada").setValue(resposta.registro.quantidadeBaixada);    
                    registro.idCodigo = resposta.registro.id.idCodigo.toString();
                    
                    if (form.lastSubmit) {
                        if (form.lastSubmit.quantidade != registro.quantidade) {
                            // Responsavel por recarregar o registro pai
                            loadParents({component: form});
                        }
                    } else {
                        // Responsavel por recarregar o registro pai
                        loadParents({component: form});                    
                    }
                    
                    form.lastSubmit = registro;       
                    // Chama a função para alterar os valores do fieldsFix
                    me.updateFieldsFix(form);
                    form.down("edicoesTopBar button[name=btnExcluir]").enable();
                    form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                },
                afterMsgSuccessTrue: function(resposta, request) {
                    // Chamada para a função de foco
                    var focusFirstOrForce = new FocusFirstOrForce();
                    focusFirstOrForce.form = form;
                    focusFirstOrForce.focus();
                },
                afterMsgSuccessFalse: function(resposta, request) {
                    form.lastSubmit = {};
                    form.down("edicoesTopBar button[name=btnSalvar]").enable();
                    if (resposta.errors) {
                        form.getForm().markInvalid(resposta.errors);
                        // Chamada para a função de foco
                        var focusFirstOrForce = new FocusFirstOrForce();
                        focusFirstOrForce.forceFocus = form.getForm().findField(resposta.errors[0].id);
                        focusFirstOrForce.focusDelay = FOCUSDELAYERRO;
                        focusFirstOrForce.focus();
                    } else {
                        // Chamada para a função de foco
                        var focusFirstOrForce = new FocusFirstOrForce();
                        focusFirstOrForce.form = form;
                        focusFirstOrForce.focus();
                    }
                },
                afterMsgFailure: function(resposta, request) {
                    form.lastSubmit = {};
                    form.down("edicoesTopBar button[name=btnSalvar]").enable();
                    if (resposta.errors) {
                        // Chamada para a função de foco
                        var focusFirstOrForce = new FocusFirstOrForce();
                        focusFirstOrForce.forceFocus = form.getForm().findField(resposta.errors[0].id);
                        focusFirstOrForce.focusDelay = FOCUSDELAYERRO;
                        focusFirstOrForce.focus();
                    } else {
                        // Chamada para a função de foco
                        var focusFirstOrForce = new FocusFirstOrForce();
                        focusFirstOrForce.form = form;
                        focusFirstOrForce.focus();
                    }
                }
            };

            requestServerJson(request);
        }
    },
    
    excluirRegistro: function(form, registros) {        
        var me = this;        
        var request = {
            async: true,
            showMsgErrors: true,
            url: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/delete",
            parametros: Ext.JSON.encode({
                registros: registros
            }),
            component: form,
            waitMsg: DESTROYMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.botaoTopBarNovo(form.down("edicoesTopBar button[name=btnNovo]"));
                
                // Responsavel por recarregar o registro pai
                loadParents({component: form});                
                
                for (var i in resposta.registros) {                                        
                    var f = resposta.registros[i];                                        
                    if (f.quantidadeBaixada) {                     
                        form.getForm().findField("quantidadeBaixada").setValue(f.quantidadeBaixada);                                                                                               
                        form.getForm().findField("quantidade").setValue(form.getForm().findField("quantidadeEntregar").getValue() - f.quantidadeBaixada);                                                    
                    }                    
                }
                
                // Chama a função para alterar os valores do fieldsFix
                me.updateFieldsFix(form);
            }
        };
        
        requestServerJson(request);
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
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "vasilhameEntregarItem") {
                            form.getForm().findField("idVasilhameEntregarItem").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
            }           
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
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "id") {
                            form.getForm().findField("idUsuario").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("nomeUsuario").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    updateFieldsFix: function(form) {
        if (form.params) {
            if (form.params.fieldsFix) {
                for (var i in form.params.fieldsFix) {
                    var f = form.params.fieldsFix[i];
                    if (f.field == "quantidade") {
                        var qtdEnt = form.getForm().findField("quantidadeEntregar").getValue();
                        var qtdBai = form.getForm().findField("quantidadeBaixada").getValue();                        
                        var val = Math.round((parseFloat(qtdEnt) - parseFloat(qtdBai)) * 1000) / 1000;                                                
                        f.value = val == null ? 0 : val;                        
                    }
                    if (f.field == "quantidadeBaixada") {
                        var val = form.getForm().findField("quantidadeBaixada").getValue();
                        f.value = val == null ? 0 : val;
                    }
                }
            }
        }
    }

});