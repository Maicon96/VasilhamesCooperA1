Ext.define("App.controller.vasilhame.entregues.EntregueCad", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.cadastro.Window",
        "vasilhame.entregues.cadastro.Form",
        "vasilhame.entregues.cadastro.ButtonGroup",
        "vasilhame.entregues.cadastro.TabPanel",
        "vasilhame.entregues.cadastro.Itens"
    ],
    
    init: function() {
        this.control ({
            "entregueCadWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregueCadForm > edicoesTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregueCadForm > edicoesTopBar button[name=btnSalvar]": {
                click: this.botaoTopBarSalvar
            },
            "entregueCadForm > edicoesTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir,
                disable: this.botaoTopBarStatus,
                enable: this.botaoTopBarStatus
            },
            "entregueCadForm > edicoesTopBar button[name=btnFinalizar]": {
                click: this.botaoTopBarFinalizar
            },
            "entregueCadForm > edicoesTopBar button[name=btnAbrir]": {
                click: this.botaoTopBarAbrir
            },
            "entregueCadForm > edicoesTopBar button[name=btnInutilizar]": {
                click: this.botaoTopBarInutilizar
            },
            "entregueCadForm > edicoesTopBar button[name=btnPDF]": {
                click: this.botaoTopBarPDF
            },
            "entregueCadForm > edicoesTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueCadForm > edicoesTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregueCadForm textfield[name=idEmpresa]": {
                focus: this.onFocus,
                blur: this.empresaBlur
            },
            "entregueCadForm button[name=conEmpresas]": {
                click: this.conEmpresasClick
            },
            "entregueCadForm textfield[name=idFilial]": {
                focus: this.onFocus,
                blur: this.filialBlur
            },
            "entregueCadForm button[name=conFiliais]": {
                click: this.conFiliaisClick
            },
            "entregueCadForm textfield[name=idPessoa]": {
                focus: this.onFocus,
                blur: this.pessoaBlur,
                change: this.pessoaChange
            },
            "entregueCadForm button[name=conPessoas]": {
                click: this.conPessoasClick
            },
            "entregueCadForm textfield[name=idUsuario]": {
                focus: this.onFocus,
                blur: this.usuarioBlur
            },
            "entregueCadForm button[name=conUsuarios]": {
                click: this.conUsuariosClick
            },
            "entregueCadForm textfield[name=observacao]": {
                focus: this.onFocus,
                keydown: this.observacaoKeyDown
            },
            
            "entregueCadForm button[name=item]": {
                click: this.botaoBotBarItemClick
            },
            "entregueCadForm menuitem[name=item]": {
                click: this.botaoBotBarItemClick
            }
        });
    },

    onReady: function(window) {        
        var me = this;
        var form = window.down("entregueCadForm");
        
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
        var form = button.up("entregueCadForm");
        //propriedade que serve para saber quando é um registro novo
        form.novo = true;
        me.filtros(form, 0, "", 0, "", 0, 0, "", "", "");
        me.limparTela(form);
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.form = form;
        focusFirstOrForce.focus();
        
        //serve para deixar readOnly os campos contribuinte e cpfcnpj
        me.pessoaChange(form.getForm().findField("idPessoa"), form.getForm().findField("idPessoa").getValue());
        form.getForm().findField("idFilial").setValue(USUARIOFIL);
        form.getForm().findField("descricaoFilial").setValue(USUARIOFILDES);
        form.getForm().findField("dataHoraGravacao").setValue(new Date);
        form.getForm().findField("idUsuario").setValue(USUARIOLOG);
        form.getForm().findField("nomeUsuario").setValue(USUARIONOM);
    },
    
    botaoTopBarSalvar: function(button) {
        var me = this;
        var form = button.up("entregueCadForm");
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
        
        if (form.getForm().findField("idPessoa").getValue() != "") {  
            var pessoaCodigoDigito = getCodigoDigito(dados.idPessoa);
            dados.idPessoa = pessoaCodigoDigito.codigo; 
            dados.pessoa = {
                id: {
                    idEmpresa: form.getForm().findField("idEmpresa").getValue(),              
                    idPessoa: pessoaCodigoDigito.codigo
                },
                digito: pessoaCodigoDigito.digito            
            };       
        }
                
        dados.id = {
            idCodigo: form.getForm().findField("idCodigo").getValue(),
            idEmpresa: form.getForm().findField("idEmpresa").getValue(),
            idFilial: form.getForm().findField("idFilial").getValue()
        };
        
        me.gravaRegistro(form, dados);
    },

    botaoTopBarExcluir: function(button) {
        var me = this;
        var form = button.up("entregueCadForm");
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
                    idCodigo: form.getForm().findField("idCodigo").getValue()
                }];
                me.excluirRegistro(form, registros);
            }
        });         
    },
    
    botaoTopBarStatus: function(button) {
        var form = button.up("entregueCadForm");
        form.valueComparatorExternal = button.isDisabled();
    },
    
    botaoTopBarFinalizar: function(button) {
        var form = button.up("entregueCadForm");
        var empresa = form.getForm().findField("idEmpresa").getValue();
        var filial = form.getForm().findField("idFilial").getValue();
        var id = form.getForm().findField("idCodigo").getValue(); 
        var situacao = form.getForm().findField("situacao").getValue(); 
        
        if (situacao != 2) {
            if (situacao == 1) {    
                var registro = {
                    idEmpresa: empresa,
                    idFilial: filial,
                    idCodigo: id,
                    situacao: situacao
                };                

                var request = {
                    async: true,
                    showMsgErrors: true,
                    url: BACKEND + "/vasilhame/entregues/cadastro/finalizar",
                    parametros: Ext.JSON.encode({
                        registro: registro
                    }),
                    component: form,
                    btnSalvar: form.down("edicoesTopBar button[name=btnSalvar]"),
                    waitMsg: "Finalizando Vasilhame",
                    beforeMsgSuccessTrue: function(resposta, request) {                                              
                        form.getForm().findField("situacao").setValue(resposta.registro.situacao);  
//                        form.down("edicoesTopBar button[name=btnFinalizar]").disable(); 
//                        form.down("edicoesTopBar button[name=btnAbrir]").enable();
//                        form.down("edicoesTopBar button[name=btnInutilizar]").enable();
//                        form.down("edicoesTopBar button[name=btnPDF]").enable();
                        
                    },
//                    afterMsgSuccessTrue: function(resposta, request) {                            
//                        // Chamada para a função de mensagens ao usuário    
//                        var callMessageBox = new CallMessageBox();
//                        callMessageBox.title = "Sucesso";
//                        callMessageBox.msg = "Vasilhame finalizado com sucesso!";
//                        //callMessageBox.icon = Ext.MessageBox.WARNING;
//                        callMessageBox.buttons = Ext.MessageBox.OK;
//                        callMessageBox.show({
//                            afterClickBtnOK: function(btn) {                                    
//                                // Chamada para a função de foco
//                                var focusFirstOrForce = new FocusFirstOrForce();
//                                focusFirstOrForce.form = form;
//                                focusFirstOrForce.focus();
//                            }
//                        }); 
//                    },
                    afterMsgSuccessFalse: function(resposta, request) {             
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
                    }                
                };

                requestServerJson(request);    
            } else {
                var callMessageBox = new CallMessageBox();
                callMessageBox.title = "Erro";
                callMessageBox.msg = "Não foi possível finalizar o vasilhame entregue, situação precisa ser aberto";
                callMessageBox.icon = Ext.MessageBox.ERROR;
                callMessageBox.buttons = Ext.MessageBox.OK;
                callMessageBox.show();
            }                       
        } else {
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Atenção";
            callMessageBox.msg = "Vasilhame já está finalizado";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        }
        
    },
    
    botaoTopBarAbrir: function(button) {
        var form = button.up("entregueCadForm");
        var empresa = form.getForm().findField("idEmpresa").getValue();
        var filial = form.getForm().findField("idFilial").getValue();
        var id = form.getForm().findField("idCodigo").getValue(); 
        var situacao = form.getForm().findField("situacao").getValue(); 
        
         if (situacao != 1) {            
            var registro = {
                idEmpresa: empresa,
                idFilial: filial,
                idCodigo: id,
                situacao: situacao
            };                

            var request = {
                async: true,
                showMsgErrors: true,
                url: BACKEND + "/vasilhame/entregues/cadastro/abrir",
                parametros: Ext.JSON.encode({
                    registro: registro
                }),
                component: form,
                btnSalvar: form.down("edicoesTopBar button[name=btnSalvar]"),
                waitMsg: "Abrindo Vasilhame",
                beforeMsgSuccessTrue: function(resposta, request) {
                    form.getForm().findField("situacao").setValue(resposta.registro.situacao);    
//                      form.down("edicoesTopBar button[name=btnFinalizar]").enable();    
//                      form.down("edicoesTopBar button[name=btnInutilizar]").enable();
                },
//                afterMsgSuccessTrue: function(resposta, request) {                    
//                    // Chamada para a função de mensagens ao usuário    
//                    var callMessageBox = new CallMessageBox();
//                    callMessageBox.title = "Sucesso";
//                    callMessageBox.msg = "Vasilhame aberto com sucesso!";
//                    //callMessageBox.icon = Ext.MessageBox.WARNING;
//                    callMessageBox.buttons = Ext.MessageBox.OK;
//                    callMessageBox.show({
//                        afterClickBtnOK: function(btn) {                                    
//                            // Chamada para a função de foco
//                            var focusFirstOrForce = new FocusFirstOrForce();
//                            focusFirstOrForce.form = form;
//                            focusFirstOrForce.focus();
//                        }
//                    }); 
//                },
                afterMsgSuccessFalse: function(resposta, request) {             
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
                }                
            };

            requestServerJson(request);      
                                  
        } else {
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Atenção";
            callMessageBox.msg = "Vasilhame já está aberto";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        }                      
    },
    
    botaoTopBarInutilizar: function(button) {
        var form = button.up("entregueCadForm");
        var empresa = form.getForm().findField("idEmpresa").getValue();
        var filial = form.getForm().findField("idFilial").getValue();
        var id = form.getForm().findField("idCodigo").getValue(); 
        var situacao = form.getForm().findField("situacao").getValue(); 
        
        if (situacao != 8) {            
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Inutilizar";
            callMessageBox.msg = "Deseja realmente inutilizar o vasilhame?";
            callMessageBox.defaultFocus = "no";
            callMessageBox.icon = Ext.MessageBox.QUESTION;
            callMessageBox.buttons = Ext.MessageBox.YESNO;
            callMessageBox.show({
                afterClickBtnYES: function(btn) {
                    var registro = {
                        idEmpresa: empresa,
                        idFilial: filial,
                        idCodigo: id,
                        situacao: situacao
                    };                

                    var request = {
                        async: true,
                        showMsgErrors: true,
                        url: BACKEND + "/vasilhame/entregues/cadastro/inutilizar",
                        parametros: Ext.JSON.encode({
                            registro: registro
                        }),
                        component: form,
                        btnSalvar: form.down("edicoesTopBar button[name=btnSalvar]"),
                        waitMsg: "Inutilizando Vasilhame",
                        beforeMsgSuccessTrue: function(resposta, request) {
                            form.getForm().findField("situacao").setValue(resposta.registro.situacao);                                                    
                        },
//                        afterMsgSuccessTrue: function(resposta, request) {                                                                                    
//                            // Chamada para a função de mensagens ao usuário    
//                            var callMessageBox = new CallMessageBox();
//                            callMessageBox.title = "Sucesso";
//                            callMessageBox.msg = "Vasilhame inutilizado com sucesso!";
//                            //callMessageBox.icon = Ext.MessageBox.WARNING;
//                            callMessageBox.buttons = Ext.MessageBox.OK;
//                            callMessageBox.show({
//                                afterClickBtnOK: function(btn) {                                    
//                                    // Chamada para a função de foco
//                                    var focusFirstOrForce = new FocusFirstOrForce();
//                                    focusFirstOrForce.form = form;
//                                    focusFirstOrForce.focus();
//                                }
//                            });     
//                        },
                        afterMsgSuccessFalse: function(resposta, request) {             
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
                        }                
                    };
                    requestServerJson(request);    
                }
            });                                            
        } else {
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Atenção";
            callMessageBox.msg = "Vasilhame já está inutilizado";
            callMessageBox.icon = Ext.MessageBox.WARNING;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        }                   
    },
    
    botaoTopBarPDF: function(button) {
        var form = button.up("entregueCadForm");         
        var situacao = form.getForm().findField("situacao").getValue();        
        if (situacao != 1) {
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            var idFilial = form.getForm().findField("idFilial").getValue();
            var idCodigo = form.getForm().findField("idCodigo").getValue();

            var vasilhameEntregue = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: idCodigo,
                situacao: situacao,
                id: {
                    idEmpresa: idEmpresa,
                    idFilial: idFilial,
                    idCodigo: idCodigo
                }           
            };    

            var chave = {
                vasilhameEntregue: vasilhameEntregue            
            };                             

            var formato = "PDF";    
            var url = BACKEND + "/vasilhame/entregues/comprovante/relatorio/pdf";        

            var callReport = new CallReport1();
            callReport.url = url;        
            callReport.format = formato;
            callReport.nameFile = "Vasilhames Entregues Comprovante." + formato.toLowerCase();
            callReport.records = chave;               
            callReport.call();    
        } else {
            var callMessageBox = new CallMessageBox();
            callMessageBox.title = "Erro";
            callMessageBox.msg = "Não foi possível gerar o comprovante, situação do vasilhame não pode estar aberto";
            callMessageBox.icon = Ext.MessageBox.ERROR;
            callMessageBox.buttons = Ext.MessageBox.OK;
            callMessageBox.show();
        }     
                             
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
            var form = field.up("entregueCadForm");
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
        var form = button.up("entregueCadForm");
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
            var form = field.up("entregueCadForm");
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
        var form = button.up("entregueCadForm");
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
    
    pessoaBlur: function (field) {
        if (field.getValue() != field.oldValue) {
            var me = this;            
            var form = field.up("entregueCadForm");
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if ( (isNaN(idEmpresa)) || (idEmpresa == "") || (idEmpresa == null) ) {
                idEmpresa = 0;
            }
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue == null) ) {
                form.getForm().findField("nome").setValue("");
                form.getForm().findField("cpfcnpj").setValue("");
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
        var form = button.up("entregueCadForm");
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
        var idCodigo = form.getForm().findField("idCodigo").getValue();                
        if (idCodigo == null || idCodigo == "") {
            callProgram.fieldsFix.push({
                field: "situacao", 
                value: "1",
                type: "string",
                comparison: "eq",
                connector: "AND",
                filterable: true,
                considerSetValue: false
            });
        }
        callProgram.call();        
    },
    
    pessoaChange: function(field, newValue, oldValue) {
        var form = field.up("entregueCadForm");
        if (field.getValue() === "" || field.getValue() === null) {
            form.getForm().findField("nome").setReadOnly(false);
            form.getForm().findField("tipoContribuinte").setReadOnly(false);
            form.getForm().findField("cpfcnpj").setReadOnly(false);
        }
        else {
            form.getForm().findField("nome").setReadOnly(true);
            form.getForm().findField("tipoContribuinte").setReadOnly(true);
            form.getForm().findField("cpfcnpj").setReadOnly(true);
        }
    },
    
    usuarioBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueCadForm");
            var usuario = field.getValue();
            if (usuario == "" || usuario == null) {
                form.getForm().findField("nomeUsuario").setValue("");
                return false;
            }
            var registro = {
                login: usuario
            };
            me.carregaUsuario(form, registro);
        }
    },
    
    conUsuariosClick: function(button) {
        var me = this;
        var form = button.up("entregueCadForm");
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
    
    observacaoKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregueCadForm");
                if (!form.down("edicoesTopBar button[name=btnSalvar]").isDisabled()) {
                    me.botaoTopBarSalvar(form.down("edicoesTopBar button[name=btnSalvar]"));
                }
            }
        }
    },    
    
    botaoBotBarItemClick: function(button) {  
        var form = button.up("form");
        if (button.id.indexOf("menuitem") !== -1) {
            button = button.up("entregueCadButtonGroup").down("toolbar").down("button[name=" + button.name + "]");
        }
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemCon";
        
        if (!button.fieldsFix) {
            callProgram.fieldsFix = new Array();
            callProgram.fieldsFix.push({
                field: "idEmpresa", 
                fieldBtn: "conEmpresas", 
                value: 0,
                type: "numeric",
                comparison: "eq",
                connector: "AND",
                readOnly: true,
                filterable: true
            });
            callProgram.fieldsFix.push({
                field: "idFilial", 
                fieldBtn: "conFiliais", 
                value: 0,
                type: "numeric",
                comparison: "eq",
                connector: "AND",
                readOnly: true,
                filterable: true
            });
            callProgram.fieldsFix.push({
                field: "idVasilhameEntregue", 
                fieldBtn: "conVasilhames", 
                value: 0,
                type: "numeric",
                comparison: "eq",
                connector: "AND",
                readOnly: true,
                filterable: true
            });
        }
        else {
            callProgram.fieldsFix = button.fieldsFix;
        }
        callProgram.columnsHidden = new Array();
        callProgram.columnsHidden.push({
            dataIndex: "idEmpresa", 
            value: true
        }); 
        callProgram.columnsHidden.push({
            dataIndex: "idFilial", 
            value: true
        }); 
        callProgram.columnsHidden.push({
            dataIndex: "idVasilhameEntregue", 
            value: true
        }); 
        
        // Pega o parametro e envia para o registro filho
        if (button.up("entregueCadForm").params) {
            if (button.up("entregueCadForm").params.loadParents) {
                callProgram.loadParents = button.up("entregueCadForm").params.loadParents;
            }
        }
        if (form.hasOwnProperty("novo")) {
            callProgram.novo = form.novo;    
        }
        callProgram.call();
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
        form.down("edicoesTopBar button[name=btnFinalizar]").disable();
        form.down("edicoesTopBar button[name=btnAbrir]").disable();
        form.down("edicoesTopBar button[name=btnInutilizar]").disable();
        form.down("edicoesTopBar button[name=btnPDF]").disable();
        setFieldsFix(form);
        
        if (form.modoCompl == "abas") {
            var gridItem = form.down("entregueItemConGrid");            
            gridItem.down("consultasTopBar button[name=btnNovo]").disable();
            gridItem.down("consultasTopBar button[name=btnAlterar]").disable();
            gridItem.down("consultasTopBar button[name=btnExcluir]").disable();
            gridItem.down("combo[name=perpage]").setValue(Ext.util.Format.leftPad(gridItem.store.pageSize, 2, "0"));
        }
        if (form.modoCompl == "botoes") {
            form.down("entregueCadButtonGroup").down("toolbar").disable();            
        }
    },    
    
    carregaRegistro: function(form, registro, focus) {
        var me = this;       
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/cadastro/exists",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.novo = false;
                me.limparTela(form);                              
                form.getForm().setValues(resposta.registro);                
                form.getForm().setValues({
                    idEmpresa: resposta.registro.idEmpresa,
                    nomeEmpresa: resposta.registro.empresa.nome,
                    idFilial: resposta.registro.idFilial,
                    descricaoFilial: resposta.registro.filial.descricao,
                    idCodigo: resposta.registro.idCodigo,
                    idPessoa: resposta.registro.pessoa.codigoDigito,     
                    nome: resposta.registro.nome,
                    tipoContribuinte: resposta.registro.tipoContribuinte,
                    cpfcnpj: resposta.registro.cpfcnpj,
                    observacao: resposta.registro.observacao,
                    situacao: resposta.registro.situacao,
                    dataHoraGravacao: resposta.registro.dataHoraGravacao,
                    idUsuario: resposta.registro.idUsuario,
                    nomeUsuario: resposta.registro.usuario.nome
                });
                
                //serve para deixar readOnly os campos contribuinte e cpfcnpj
                me.pessoaChange(form.getForm().findField("idPessoa"), form.getForm().findField("idPessoa").getValue());
                
                form.down("edicoesTopBar button[name=btnExcluir]").enable();   
                form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                form.down("edicoesTopBar button[name=btnFinalizar]").enable();    
                form.down("edicoesTopBar button[name=btnAbrir]").enable();
                form.down("edicoesTopBar button[name=btnInutilizar]").enable();
                form.down("edicoesTopBar button[name=btnPDF]").enable();
                    
//                if (resposta.registro.situacao == 1) {                    
//                    form.down("edicoesTopBar button[name=btnFinalizar]").enable();    
//                    form.down("edicoesTopBar button[name=btnInutilizar]").enable();
//                } else if (resposta.registro.situacao == 2) {                    
//                    form.down("edicoesTopBar button[name=btnAbrir]").enable();
//                    form.down("edicoesTopBar button[name=btnInutilizar]").enable();
//                    form.down("edicoesTopBar button[name=btnPDF]").enable();
//                } else if (resposta.registro.situacao == 8 || resposta.registro.situacao == 9) {                                        
//                    form.down("edicoesTopBar button[name=btnPDF]").enable();                
//                }
                
                if (form.modoCompl == "abas") {
                    var gridItem = form.down("entregueItemConGrid");
                    gridItem.down("consultasTopBar button[name=btnNovo]").enable();
                    gridItem.down("consultasTopBar button[name=btnAlterar]").enable();
                    gridItem.down("consultasTopBar button[name=btnExcluir]").enable();
                }
                if (form.modoCompl == "botoes") {
                    form.down("entregueCadButtonGroup").down("toolbar").enable();
                }                   
                
                var pessoa = "";
                if (resposta.registro.idPessoa) {                    
                    if (resposta.registro.idPessoa != null) {                        
                        pessoa = resposta.registro.idPessoa + resposta.registro.pessoa.digito;                                    
                    }                    
                }             
                    
                me.filtros(form, resposta.registro.idEmpresa, resposta.registro.empresa.nome, 
                    resposta.registro.idFilial, resposta.registro.filial.descricao, resposta.registro.idCodigo,
                    pessoa, resposta.registro.nome, resposta.registro.tipoContribuinte, resposta.registro.cpfcnpj); 
                
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
                
                //serve para deixar readOnly os campos contribuinte e cpfcnpj
                me.pessoaChange(form.getForm().findField("idPessoa"), form.getForm().findField("idPessoa").getValue());
                
                form.down("edicoesTopBar button[name=btnSalvar]").enable();
                me.filtros(form, 0, "", 0, "", 0, 0, "", "", "");
                
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
            var url = "/vasilhame/entregues/cadastro/save";
            if (registro.idCodigo) {
                url = "/vasilhame/entregues/cadastro/update";
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
                    registro.idCodigo = resposta.registro.id.idCodigo.toString();
                    form.lastSubmit = registro;        
                    form.down("edicoesTopBar button[name=btnExcluir]").enable();
                    form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                    form.down("edicoesTopBar button[name=btnFinalizar]").enable();
                    form.down("edicoesTopBar button[name=btnAbrir]").enable();
                    form.down("edicoesTopBar button[name=btnInutilizar]").enable();
                    form.down("edicoesTopBar button[name=btnPDF]").enable();
                    
                    if (form.modoCompl == "abas") {
                        var gridItem = form.down("entregueItemConGrid");
                        gridItem.down("consultasTopBar button[name=btnNovo]").enable();
                        gridItem.down("consultasTopBar button[name=btnAlterar]").enable();
                        gridItem.down("consultasTopBar button[name=btnExcluir]").enable();
                    }
                    if (form.modoCompl == "botoes") {
                        form.down("entregueCadButtonGroup").down("toolbar").enable();
                    }   
                    
                    var pessoa = "";
                    if (resposta.registro.idPessoa) {                    
                        if (resposta.registro.idPessoa != null) {                        
                            pessoa = resposta.registro.idPessoa + resposta.registro.pessoa.digito;                                    
                        }                    
                    }                                    
                                 
                    me.filtros(form, resposta.registro.idEmpresa, form.getForm().findField("nomeEmpresa").getValue(),
                        resposta.registro.idFilial, form.getForm().findField("descricaoFilial").getValue(),
                        resposta.registro.id.idCodigo, pessoa, resposta.registro.nome, resposta.registro.tipoContribuinte,
                        resposta.registro.cpfcnpj);
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
            url: BACKEND + "/vasilhame/entregues/cadastro/delete",
            parametros: Ext.JSON.encode({
                registros: registros
            }),
            component: form,
            waitMsg: DESTROYMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.botaoTopBarNovo(form.down("edicoesTopBar button[name=btnNovo]"));                
            }
        };
        
        requestServerJson(request);
    },
    
    carregaPessoa: function (form, registro) {
        var url = BACKEND + "/vasilhame/entregues/cadastro/exists/pessoa";
        if (form.down("edicoesTopBar button[name=btnExcluir]").disabled) {
          url = BACKEND + "/vasilhame/entregues/cadastro/exists/pessoa/ativa";
        }                
        
        var request = {
            async: true,
            showMsgErrors: false,
            url: url,
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idPessoa"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function (resposta, request) {
                form.getForm().findField("nome").setValue(resposta.registro.nome);
                form.getForm().findField("tipoContribuinte").setValue(resposta.registro.contribuinte);
                form.getForm().findField("cpfcnpj").setValue(resposta.registro.cpfCnpj);
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
                form.getForm().findField("cpfcnpj").setValue("");
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
    
    carregaRegistrosLoadParents: function(form, registro) {
        var me = this;        
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/cadastro/consultasituacao",            
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().setValues({
                    situacao: resposta.registro.situacao                                        
                });                
            }
        };
        
        requestServerJson(request);
    },
    
    filtros: function (form, idEmpresa, nomeEmpresa, idFilial, descricaoFilial, idVasilhameEntregue, idPessoa,
            nome, contribuinte, cpfcnpj) {        
        var me = this;
        var filtrosItens = [];          
        filtrosItens.push({
            field: "idEmpresa",
            fieldBtn: "conEmpresas", 
            value: idEmpresa,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        filtrosItens.push({
            field: "nomeEmpresa", 
            value: nomeEmpresa,
            readOnly: true,
            filterable: false
        });
        filtrosItens.push({
            field: "idFilial",
            fieldBtn: "conFiliais", 
            value: idFilial,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        }); 
        filtrosItens.push({
            field: "descricaoFilial", 
            value: descricaoFilial,
            readOnly: true,
            filterable: false
        });
        filtrosItens.push({
            field: "idVasilhameEntregue",
            fieldBtn: "conVasilhames", 
            value: idVasilhameEntregue,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        }); 
        filtrosItens.push({
            field: "idPessoa",
            fieldBtn: "conPessoas", 
            value: idPessoa,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: false
        });      
        filtrosItens.push({
            field: "nome", 
            value: nome,
            readOnly: true,
            filterable: false
        });
        filtrosItens.push({
            field: "tipoContribuinte", 
            value: contribuinte,
            readOnly: true,
            filterable: false
        });
        filtrosItens.push({
            field: "cpfcnpj", 
            value: cpfcnpj,
            readOnly: true,
            filterable: false
        });               
        
        if (form.modoCompl == "abas") {            
            var gridItem = form.down("entregueItemConGrid");            
            // Pega o parametro e envia para o registro filho             
            // Recupera todos os dados do form para depois que fizer o recalcular fazer o exists
            var dados = form.getForm().getValues();        
            
            var chave = {
                idEmpresa: form.getForm().findField("idEmpresa").getValue(),
                idFilial: form.getForm().findField("idFilial").getValue(),
                idCodigo: form.getForm().findField("idCodigo").getValue()
            };
            gridItem.params.loadParents = {
                controller: me,
                funcao: "carregaRegistrosLoadParents",
                params: [form, chave]
            };
            
            gridItem.params.fieldsFix = filtrosItens;
            if (form.hasOwnProperty("novo")) {
                gridItem.params.novo = form.novo;    
            }
            setFilterFix(gridItem);
            if (!form.down("edicoesTopBar button[name=btnExcluir]").disabled) {
                if (form.down("entregueCadTabPanel").activeTab.xtype == "entregueCadItem") {
                    gridItem.store.currentPage = 1;
                    gridItem.store.load();
                }
            }
        }
        if (form.modoCompl == "botoes") {
            form.down("entregueCadButtonGroup").down("toolbar").down("button[name=item]").fieldsFix = filtrosItens;
        }
    }

});