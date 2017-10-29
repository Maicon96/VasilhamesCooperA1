Ext.define("App.controller.vasilhame.entregues.itens.baixas.cupons.EntregueItemBaixaCupomCad", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.itens.baixas.cupons.cadastro.Window",
        "vasilhame.entregues.itens.baixas.cupons.cadastro.Form"
    ],
    
    init: function() {
        this.control ({
            "entregueItemBaixaCupomCadWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregueItemBaixaCupomCadForm > edicoesTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregueItemBaixaCupomCadForm > edicoesTopBar button[name=btnSalvar]": {
                click: this.botaoTopBarSalvar
            },
            "entregueItemBaixaCupomCadForm > edicoesTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir
            },
            "entregueItemBaixaCupomCadForm > edicoesTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueItemBaixaCupomCadForm > edicoesTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregueItemBaixaCupomCadForm textfield[name=idEmpresaBaixa]": {
                focus: this.onFocus,
                blur: this.empresaBaixaBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conEmpresasBaixas]": {
                click: this.conEmpresasBaixasClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=idFilialBaixa]": {
                focus: this.onFocus,
                blur: this.filialBaixaBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conFiliaisBaixas]": {
                click: this.conFiliaisBaixasClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=idVasilhameEntregueItemBaixa]": {
                focus: this.onFocus,
                blur: this.vasilhameItemBaixaBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conVasilhamesItensBaixas]": {
                click: this.conVasilhamesItensBaixasClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=idEmpresaCupom]": {
                focus: this.onFocus,
                blur: this.empresaCupomBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conEmpresasCupons]": {
                click: this.conEmpresasCuponsClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=idFilialCupom]": {
                focus: this.onFocus,
                blur: this.filialCuponsBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conFiliaisCupons]": {
                click: this.conFiliaisCuponsClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=idEcfCupom]": {
                focus: this.onFocus,
                blur: this.ecfBlur
            },
            "entregueItemBaixaCupomCadForm button[name=conECFs]": {
                click: this.conECFsClick
            },
            "entregueItemBaixaCupomCadForm textfield[name=sequencia]": {
                keydown: this.sequenciaKeyDown
            }
        });
    },

    onReady: function(window) {     
        var me = this;
        var form = window.down("entregueItemBaixaCupomCadForm");
        
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
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.limparTela(form);
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.form = form;
        focusFirstOrForce.focus();    
        
        if (form.params) {
            if (!form.params.fieldsFix) {
                form.getForm().findField("idFilialBaixa").setValue(USUARIOFIL);
                form.getForm().findField("descricaoFilialBaixa").setValue(USUARIOFILDES);
            }            
        }
        form.getForm().findField("idFilialCupom").setValue(USUARIOFIL);
        form.getForm().findField("descricaoFilialCupom").setValue(USUARIOFILDES);
    },
    
    botaoTopBarSalvar: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
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
            idEmpresaBaixa: form.getForm().findField("idEmpresaBaixa").getValue(),
            idFilialBaixa: form.getForm().findField("idFilialBaixa").getValue(),
            idVasilhameEntregueItemBaixa: form.getForm().findField("idVasilhameEntregueItemBaixa").getValue(),
            idEmpresaCupom: form.getForm().findField("idEmpresaCupom").getValue(),
            idFilialCupom: form.getForm().findField("idFilialCupom").getValue(),
            idEcfCupom: form.getForm().findField("idEcfCupom").getValue(),
            numero: form.getForm().findField("numero").getValue(),
            dataEmissao: Ext.util.Format.date(form.getForm().findField("dataEmissao").getValue(), "Y-m-d"),
            sequencia: form.getForm().findField("sequencia").getValue()
        };
        
        me.gravaRegistro(form, dados);
    },

    botaoTopBarExcluir: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
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
                    idEmpresaBaixa: form.getForm().findField("idEmpresaBaixa").getValue(),
                    idFilialBaixa: form.getForm().findField("idFilialBaixa").getValue(),
                    idVasilhameEntregueItemBaixa: form.getForm().findField("idVasilhameEntregueItemBaixa").getValue(),
                    idEmpresaCupom: form.getForm().findField("idEmpresaCupom").getValue(),
                    idFilialCupom: form.getForm().findField("idFilialCupom").getValue(),
                    idEcfCupom: form.getForm().findField("idEcfCupom").getValue(),
                    numero: form.getForm().findField("numero").getValue(),
                    dataEmissao: Ext.util.Format.date(form.getForm().findField("dataEmissao").getValue(), "Y-m-d"),
                    sequencia: form.getForm().findField("sequencia").getValue()
                };

                var registros = [{
                    id: id,
                    idEmpresaBaixa: form.getForm().findField("idEmpresaBaixa").getValue(),
                    idFilialBaixa: form.getForm().findField("idFilialBaixa").getValue(),
                    idVasilhameEntregueItemBaixa: form.getForm().findField("idVasilhameEntregueItemBaixa").getValue(),
                    idEmpresaCupom: form.getForm().findField("idEmpresaCupom").getValue(),
                    idFilialCupom: form.getForm().findField("idFilialCupom").getValue(),
                    idEcfCupom: form.getForm().findField("idEcfCupom").getValue(),
                    numero: form.getForm().findField("numero").getValue(),
                    dataEmissao: Ext.util.Format.date(form.getForm().findField("dataEmissao").getValue(), "Y-m-d"),
                    sequencia: form.getForm().findField("sequencia").getValue()
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
    
    empresaBaixaBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("nomeEmpresaBaixa").setValue("");
                return false;
            }
            var registro = {
                id: field.getValue()
            };
            form.getForm().findField("idFilialBaixa").forceLoad = true;
            me.carregaEmpresaBaixa(form, registro);
        }
    },
    
    conEmpresasBaixasClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idEmpresaBaixa"));
        var campos = new Array();
        campos[0] = {
            tabela: "id",
            form: form.getForm().findField("idEmpresaBaixa")
        };
        var funcao = {
            controller: me,
            funcao: "empresaBaixaBlur",
            parametros: [form.getForm().findField("idEmpresaBaixa")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idEmpresaBaixa")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "geempr200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    filialBaixaBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("descricaoFilialBaixa").setValue("");
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresaBaixa").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                codigo: field.getValue()
            };
            me.carregaFilialBaixa(form, registro);
        }
    },

    conFiliaisBaixasClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idFilialBaixa"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigo",
            form: form.getForm().findField("idFilialBaixa")
        };
        var funcao = {
            controller: me,
            funcao: "filialBaixaBlur",
            parametros: [form.getForm().findField("idFilialBaixa")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idFilialBaixa")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "gefili200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresaBaixa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var nomeEmpresa = form.getForm().findField("nomeEmpresaBaixa").getValue();    
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
    
    vasilhameItemBaixaBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresaBaixa").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var idFilial = form.getForm().findField("idFilialBaixa").getValue();
            if (idFilial == "") {
                idFilial = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                idFilial: idFilial,
                idCodigo: field.getValue()
            };
            me.carregaVasilhameItemBaixa(form, registro);
        }
    },

    conVasilhamesItensBaixasClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idVasilhameEntregueItemBaixa"));
        var campos = new Array();
        campos[0] = {
            tabela: "idCodigo",
            form: form.getForm().findField("idVasilhameEntregueItemBaixa")
        };
        var funcao = {
            controller: me,
            funcao: "vasilhameItemBaixaBlur",
            parametros: [form.getForm().findField("idVasilhameEntregueItemBaixa")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idVasilhameEntregueItemBaixa")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemBaixaCon";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresaBaixa").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var idFilial = form.getForm().findField("idFilialBaixa").getValue();
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
            value: form.getForm().findField("nomeEmpresaBaixa").getValue(),
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
            value: form.getForm().findField("descricaoFilialBaixa").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.call();
    },
      
    empresaCupomBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("nomeEmpresaCupom").setValue("");
                return false;
            }
            var registro = {
                id: field.getValue()
            };
            form.getForm().findField("idFilialCupom").forceLoad = true;
            me.carregaEmpresaCupom(form, registro);
        }
    },
    
    conEmpresasCuponsClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idEmpresaCupom"));
        var campos = new Array();
        campos[0] = {
            tabela: "id",
            form: form.getForm().findField("idEmpresaCupom")
        };
        var funcao = {
            controller: me,
            funcao: "empresaCupomBlur",
            parametros: [form.getForm().findField("idEmpresaCupom")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idEmpresaCupom")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "geempr200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    filialCupomBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("descricaoFilialCupom").setValue("");
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresaCupom").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                codigo: field.getValue()
            };
            me.carregaFilialCupom(form, registro);
        }
    },

    conFiliaisCuponsClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idFilialCupom"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigo",
            form: form.getForm().findField("idFilialCupom")
        };
        var funcao = {
            controller: me,
            funcao: "filialCupomBlur",
            parametros: [form.getForm().findField("idFilialCupom")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idFilialCupom")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "gefili200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresaCupom").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var nomeEmpresa = form.getForm().findField("nomeEmpresaCupom").getValue();    
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
    
    ecfBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaCupomCadForm");
            if ( (field.getValue() == "") || (field.getValue() == "0") || (field.getValue() == null) ) {
                return false;
            }
            me.carregaECFs(form);
        }
    },
    
    conECFsClick: function(button) {        
        var me = this;
        var form = button.up("entregueItemBaixaCupomCadForm");
        me.onFocus(form.getForm().findField("idEcfCupom"));
        var campos = new Array();
        campos[0] = {
            tabela: "ecf_pdecfs",
            form: form.getForm().findField("idEcfCupom")
        };
        var funcao = {
            controller: me,
            funcao: "ecfBlur",
            parametros: [form.getForm().findField("idEcfCupom")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idEcfCupom")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "pdecfs200";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        
        var idEmpresa = form.getForm().findField("idEmpresaCupom").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var idFilial = form.getForm().findField("idFilialCupom").getValue();
        if ( (isNaN(idFilial)) || (idFilial == null) || (idFilial == "") ) {
            idFilial = 0;
        }
        
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "empresa_pdecfs", 
            fieldBtn: "conEmpresasCupons", 
            value: idEmpresa,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "nome_geempr", 
            value: form.getForm().findField("nomeEmpresaCupom").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.fieldsFix = new Array();
        callProgram.fieldsFix.push({
            field: "filial_pdecfs", 
            fieldBtn: "conFiliaisCupons", 
            value: idFilial,
            type: "numeric",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        callProgram.fieldsFix.push({
            field: "descricao_gefili", 
            value: form.getForm().findField("descricaoFilialCupom").getValue(),
            readOnly: true,
            filterable: false
        });
        callProgram.call();
    },
    
    sequenciaKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregueItemBaixaCupomCadForm");
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
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.limparTela(form);                              
                form.getForm().setValues(resposta.registro);
                var produto = resposta.registro.vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto + resposta.registro.vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.digito;
                form.getForm().setValues({
                    idEmpresaBaixa: resposta.registro.idEmpresaBaixa,
                    nomeEmpresaBaixa: resposta.registro.empresaBaixa.nome,
                    idFilialBaixa: resposta.registro.idFilialBaixa,
                    descricaoFilialBaixa: resposta.registro.filialBaixa.descricao,
                    idVasilhameEntregueItemBaixa: resposta.registro.idVasilhameEntregueItemBaixa,
                    idProduto: produto,
                    descricaoProduto: resposta.registro.vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.descricao,
                    quantidade: resposta.registro.vasilhameEntregueItemBaixa.vasilhameEntregueItem.quantidade,
                    idEmpresaCupom: resposta.registro.idEmpresaCupom,
                    nomeEmpresaCupom: resposta.registro.empresaCupom.nome,
                    idFilialCupom: resposta.registro.idFilialCupom,
                    descricaoFilialCupom: resposta.registro.filialCupom.descricao,
                    idEcfCupom: resposta.registro.idEcfCupom,
                    numero: resposta.registro.numero,
                    dataEmissao: Ext.util.Format.date(resposta.registro.dataEmissao, "Y-m-d"),
                    sequencia: resposta.registro.sequencia
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
                ignoreFields.add("id", true);
                me.limparTela(form, ignoreFields);
                if (resposta.errors) {
                    form.getForm().markInvalid(resposta.errors);
                }
                
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
        var erros = form.getForm().findInvalid();
        if (erros.length > 0) {
            // Chamada para a função de foco
            var focusFirstOrForce = new FocusFirstOrForce();
            focusFirstOrForce.forceFocus = erros[0];
            focusFirstOrForce.focusDelay = FOCUSDELAYERRO;
            focusFirstOrForce.focus();
        } else {     
            var url = "/vasilhame/entregues/itens/baixas/cupons/cadastro/save";
            if (!form.down("edicoesTopBar button[name=btnExcluir]").isDisabled()) {
                url = "/vasilhame/entregues/itens/baixas/cupons/cadastro/update";
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
                    form.lastSubmit = registro;        
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
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/delete",
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
    
    carregaEmpresaBaixa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists/empresa/baixa",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idEmpresaBaixa"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeEmpresaBaixa").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "id") {
                            form.getForm().findField("idEmpresaBaixa").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("nomeEmpresaBaixa").setValue("");
            }
        };
        
        requestServerJson(request);
    },
    
    carregaFilialBaixa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists/filial/baixa",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idFilialBaixa"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("descricaoFilialBaixa").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "codigo") {
                            form.getForm().findField("idFilialBaixa").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("descricaoFilialBaixa").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaVasilhameItemBaixa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists/baixa",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idVasilhameEntregueItemBaixa"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idCodigo") {
                            form.getForm().findField("idVasilhameEntregueItemBaixa").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
            }           
        };
        
        requestServerJson(request);
    },
    
    
    carregaEmpresaCupom: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists/empresa/cupom",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idEmpresaCupom"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeEmpresaCupom").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "id") {
                            form.getForm().findField("idEmpresaCupom").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("nomeEmpresaCupom").setValue("");
            }
        };
        
        requestServerJson(request);
    },
    
    carregaFilialCupom: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/exists/filial/cupom",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idFilialCupom"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("descricaoFilialCupom").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "codigo") {
                            form.getForm().findField("idFilialCupom").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("descricaoFilialCupom").setValue("");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaECFs: function(form) {
        var filial = form.getForm().findField("idFilialCupom").getValue();  
        var ecf = form.getForm().findField("idEcfCupom").getValue();  
        var dataEmissao = form.getForm().findField("dataEmissao").getValue();
        
        var chave = {
            filial_pdecfs: filial,
            ecf_pdecfs: ecf,
            dataini: dataEmissao
        };
        var url = "ChamaBD?programa=G3BD.PDV.pdecfs100";

        var parametros = "funcao=veriExisteJSON&chave=" + escape(Ext.JSON.encode(chave));
        var xmlhttp = iniciaXMLHTTP();
        xmlhttp.open("POST", url, false);
        xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        xmlhttp.send(parametros);
        var resposta = Ext.JSON.decode(xmlhttp.responseText);
        
        if (resposta.success) {
            if (resposta.msg) {
                // Chamada para a função de mensagens ao usuário    
                var callMessageBox = new CallMessageBox();
                callMessageBox.title = "Sucesso";
                callMessageBox.msg = resposta.msg;
                callMessageBox.icon = Ext.MessageBox.INFO;
                callMessageBox.buttons = Ext.MessageBox.OK;
                callMessageBox.show();
            }
        }
        else {
            if (resposta.errors) {
                for (var i in resposta.errors) {
                    if (resposta.errors[i].id == "ecf_pdecfs") {
                        form.getForm().findField("idEcfCupom").markInvalid(resposta.errors[i].msg);
                    }
                }
            }  
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
    }

});