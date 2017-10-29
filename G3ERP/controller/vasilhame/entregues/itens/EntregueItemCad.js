Ext.define("App.controller.vasilhame.entregues.itens.EntregueItemCad", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.itens.cadastro.Window",
        "vasilhame.entregues.itens.cadastro.Form",
        "vasilhame.entregues.itens.cadastro.ButtonGroup",
        "vasilhame.entregues.itens.cadastro.TabPanel",
        "vasilhame.entregues.itens.cadastro.Baixas"
    ],
    
    init: function() {
        this.control ({
            "entregueItemCadWindow": {
                show: this.onReady,
                destroy: this.onExit
            },
            
            "entregueItemCadForm > edicoesTopBar button[name=btnNovo]": {
                click: this.botaoTopBarNovo
            },
            "entregueItemCadForm > edicoesTopBar button[name=btnSalvar]": {
                click: this.botaoTopBarSalvar
            },
            "entregueItemCadForm > edicoesTopBar button[name=btnExcluir]": {
                click: this.botaoTopBarExcluir
            },
            "entregueItemCadForm > edicoesTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueItemCadForm > edicoesTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregueItemCadForm textfield[name=idEmpresa]": {
                focus: this.onFocus,
                blur: this.empresaBlur
            },
            "entregueItemCadForm button[name=conEmpresas]": {
                click: this.conEmpresasClick
            },
            "entregueItemCadForm textfield[name=idFilial]": {
                focus: this.onFocus,
                blur: this.filialBlur
            },
            "entregueItemCadForm button[name=conFiliais]": {
                click: this.conFiliaisClick
            },
            "entregueItemCadForm textfield[name=idVasilhameEntregue]": {
                focus: this.onFocus,
                blur: this.vasilhameBlur
            },
            "entregueItemCadForm button[name=conVasilhames]": {
                click: this.conVasilhamesClick
            },
            "entregueItemCadForm textfield[name=idProduto]": {
                focus: this.onFocus,
                blur: this.produtoBlur
            },
            "entregueItemCadForm button[name=conProdutos]": {
                click: this.conProdutosClick
            },
            "entregueItemCadForm textfield[name=idUsuario]": {
                focus: this.onFocus,
                blur: this.usuarioBlur
            },
            "entregueItemCadForm button[name=conUsuarios]": {
                click: this.conUsuariosClick
            },
            "entregueItemCadForm textfield[name=situacao]": {
                focus: this.onFocus,
                keydown: this.situacaoKeyDown
            },
            
            "entregueItemCadForm button[name=baixa]": {
                click: this.botaoBotBarItemBaixaClick
            },
            "entregueItemCadForm menuitem[name=baixa]": {
                click: this.botaoBotBarItemBaixaClick
            }
        });
    },

    onReady: function(window) {        
        var me = this;
        var form = window.down("entregueItemCadForm");
        
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
        var form = button.up("entregueItemCadForm");
        me.filtros(form, 0, "", 0, "", 0, 0, "", 0, 0, 0);
        me.limparTela(form);
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
        var form = button.up("entregueItemCadForm");
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
        
        if (form.getForm().findField("idProduto").getValue() != "") {
            var produtoCodigoDigito = getCodigoDigito(dados.idProduto);

            dados.idProduto = produtoCodigoDigito.codigo; 
            dados.produto = {
                id: {
                    idEmpresa: form.getForm().findField("idEmpresa").getValue(),              
                    idProduto: produtoCodigoDigito.codigo
                },
                digito: produtoCodigoDigito.digito
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
        var form = button.up("entregueItemCadForm");
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
                    idVasilhameEntregue: form.getForm().findField("idVasilhameEntregue").getValue()                   
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
            var form = field.up("entregueItemCadForm");
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
        var form = button.up("entregueItemCadForm");
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
            var form = field.up("entregueItemCadForm");
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
        var form = button.up("entregueItemCadForm");
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
    
    vasilhameBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemCadForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == null) ) {
                form.getForm().findField("idPessoa").setValue("");
                form.getForm().findField("nome").setValue("");
                form.getForm().findField("tipoContribuinte").setValue("");
                form.getForm().findField("cpfcnpj").setValue("");
                
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
        var form = button.up("entregueItemCadForm");
        me.onFocus(form.getForm().findField("idVasilhameEntregue"));
        var campos = new Array();
        campos[0] = {
            tabela: "idCodigo",
            form: form.getForm().findField("idVasilhameEntregue")
        };
        var funcao = {
            controller: me,
            funcao: "vasilhameBlur",
            parametros: [form.getForm().findField("idVasilhameEntregue")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idVasilhameEntregue")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueCon";
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
    
    produtoBlur: function(field) {                
        if (field.getValue() != field.oldValue) {            
            var me = this;
            var form = field.up("entregueItemCadForm");
            var produto = field.getValue();
            if ( (isNaN(produto)) || (produto == "") ) {
                form.getForm().findField("descricaoProduto").setValue("");
                form.getForm().findField("tipoGarrafeira").setValue("0");
                return false;
            }
            
            var idEmpresa = form.getForm().findField("idEmpresa").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,               
                idVasilhame: getCodigoDigito(field.getValue()).codigo,
                vasilhame: {
                    idProduto: getCodigoDigito(field.getValue()).codigo,
                    digito: getCodigoDigito(field.getValue()).digito
                }
            };
            
            me.carregaProduto(form, registro);
        }
    },
    
    conProdutosClick: function(button) {        
        var me = this;
        var form = button.up("entregueItemCadForm");
        me.onFocus(form.getForm().findField("idProduto"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigoDigito",
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
        callProgram.programa = "estoques.ProdutoVasilhameCon";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;              
        
        callProgram.call();
    },
    
    usuarioBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemCadForm");
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
        var form = button.up("entregueItemCadForm");
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
    
    situacaoKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregueItemCadForm");
                if (!form.down("edicoesTopBar button[name=btnSalvar]").isDisabled()) {
                    me.botaoTopBarSalvar(form.down("edicoesTopBar button[name=btnSalvar]"));
                }
            }
        }
    },    
    
    botaoBotBarItemBaixaClick: function(button) {        
        if (button.id.indexOf("menuitem") !== -1) {
            button = button.up("entregueItemCadButtonGroup").down("toolbar").down("button[name=" + button.name + "]");
        }
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemBaixaCon";
        callProgram.fieldsFix = button.fieldsFix;
        callProgram.hiddenEmpresa = true;
        callProgram.fixEmpresa = true;
        callProgram.hiddenFilial = true;
        callProgram.fixFilial = true;
        callProgram.hiddenIdVasilhameEntregueItem = true;
        callProgram.fixIdVasilhameEntregueItem = true;
//        callProgram.programaBtnHidden = "spennp100";
        callProgram.modoCompl = "botoes";
        callProgram.openInside = true;
        callProgram.filterPad = [];
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
        setFieldsFix(form);
        
        if (form.modoCompl == "abas") {
            var gridBaixa = form.down("entregueItemBaixaConGrid");                        
            gridBaixa.down("consultasTopBar button[name=btnNovo]").disable();
            gridBaixa.down("consultasTopBar button[name=btnAlterar]").disable();
            gridBaixa.down("consultasTopBar button[name=btnExcluir]").disable();
            gridBaixa.down("combo[name=perpage]").setValue(Ext.util.Format.leftPad(gridBaixa.store.pageSize, 2, "0"));
        }
        if (form.modoCompl == "botoes") {
            form.down("entregueItemCadButtonGroup").down("toolbar").disable();            
        }
    },
    
    carregaRegistro: function(form, registro, focus) {
        var me = this;        
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/cadastro/exists",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.limparTela(form);                              
                form.getForm().setValues(resposta.registro);                
                
                var produto = "";
                if (resposta.registro.idProduto) {
                    produto = resposta.registro.idProduto + resposta.registro.produto.digito;
                }                
                
                form.getForm().setValues({
                    idEmpresa: resposta.registro.idEmpresa,
                    nomeEmpresa: resposta.registro.empresa.nome,
                    idFilial: resposta.registro.idFilial,
                    descricaoFilial: resposta.registro.filial.descricao,
                    idCodigo: resposta.registro.idCodigo,
                    idVasilhameEntregue: resposta.registro.idVasilhameEntregue,
                    idPessoa: resposta.registro.vasilhameEntregue.pessoa.codigoDigito,     
                    nome: resposta.registro.vasilhameEntregue.nome,
                    tipoContribuinte: resposta.registro.vasilhameEntregue.tipoContribuinte,
                    cpfcnpj: resposta.registro.vasilhameEntregue.cpfcnpj,
                    idProduto: produto,
                    descricaoProduto: resposta.registro.produto.descricao,
                    quantidade: resposta.registro.quantidade,                    
                    quantidadeBaixada: resposta.registro.quantidadeBaixada,                    
                    quantidadeBaixar: resposta.registro.quantidade - resposta.registro.quantidadeBaixada,                    
                    tipoGarrafeira: resposta.registro.tipoGarrafeira,
                    situacao: resposta.registro.situacao,
                    dataHoraGravacao: resposta.registro.dataHoraGravacao,
                    idUsuario: resposta.registro.idUsuario,
                    nomeUsuario: resposta.registro.usuario.nome
                });
                form.down("edicoesTopBar button[name=btnExcluir]").enable();   
                form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                
                if (form.modoCompl == "abas") {
                    var gridBaixa = form.down("entregueItemBaixaConGrid");                    
                    gridBaixa.down("consultasTopBar button[name=btnNovo]").enable();
                    gridBaixa.down("consultasTopBar button[name=btnAlterar]").enable();
                    gridBaixa.down("consultasTopBar button[name=btnExcluir]").enable();
                }
                if (form.modoCompl == "botoes") {
                    form.down("entregueItemCadButtonGroup").down("toolbar").enable();
                }                
                me.filtros(form, resposta.registro.idEmpresa, resposta.registro.empresa.nome, 
                    resposta.registro.idFilial, resposta.registro.filial.descricao, resposta.registro.idCodigo,
                    resposta.registro.idProduto + resposta.registro.produto.digito, resposta.registro.produto.descricao,
                    resposta.registro.quantidade - resposta.registro.quantidadeBaixada, resposta.registro.quantidadeBaixada,
                    resposta.registro.quantidade); 
                
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
                me.filtros(form, 0, "", 0, "", 0, 0, "", 0, 0, 0);
                
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
            var url = "/vasilhame/entregues/itens/cadastro/save";                        
            if (registro.idCodigo) {
                url = "/vasilhame/entregues/itens/cadastro/update";
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
                    form.getForm().findField("quantidadeBaixar").setValue(resposta.registro.quantidadeBaixar);                  
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
                    form.down("edicoesTopBar button[name=btnExcluir]").enable();
                    form.down("edicoesTopBar button[name=btnSalvar]").setTooltip("Alterar");
                    
                    if (form.modoCompl == "abas") {
                        var gridBaixa = form.down("entregueItemBaixaConGrid");
                        gridBaixa.down("consultasTopBar button[name=btnNovo]").enable();
                        gridBaixa.down("consultasTopBar button[name=btnAlterar]").enable();
                        gridBaixa.down("consultasTopBar button[name=btnExcluir]").enable();
                    }
                    if (form.modoCompl == "botoes") {
                        form.down("entregueItemCadButtonGroup").down("toolbar").enable();
                    }                                   
                    me.filtros(form, resposta.registro.idEmpresa, form.getForm().findField("nomeEmpresa").getValue(),
                        resposta.registro.idFilial, form.getForm().findField("descricaoFilial").getValue(),
                        resposta.registro.id.idCodigo, form.getForm().findField("idProduto").getValue(),
                        form.getForm().findField("descricaoProduto").getValue(), 
                        form.getForm().findField("quantidadeBaixar").getValue(),
                        form.getForm().findField("quantidadeBaixada").getValue(),
                        form.getForm().findField("quantidade").getValue());
                        
                    if (form.params.novo && form.params.novoProduto) {
                        me.botaoTopBarNovo(form.down("edicoesTopBar button[name=btnNovo]"));
                    }
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
            url: BACKEND + "/vasilhame/entregues/itens/cadastro/delete",
            parametros: Ext.JSON.encode({
                registros: registros
            }),
            component: form,
            waitMsg: DESTROYMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                me.botaoTopBarNovo(form.down("edicoesTopBar button[name=btnNovo]"));                
                // Responsavel por recarregar o registro pai
                loadParents({component: form});
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    form.getForm().markInvalid(resposta.errors);
                }
            }
        };
        
        requestServerJson(request);
    },
    
    carregaVasilhame: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/cadastro/exists/vasilhame",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idVasilhameEntregue"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {                
                form.getForm().findField("idPessoa").setValue(resposta.registro.pessoa.codigoDigito);
                form.getForm().findField("nome").setValue(resposta.registro.nome);
                form.getForm().findField("tipoContribuinte").setValue(resposta.registro.tipoContribuinte);
                form.getForm().findField("cpfcnpj").setValue(resposta.registro.cpfcnpj);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idCodigo") {
                            form.getForm().findField("idVasilhameEntregue").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("idPessoa").setValue("");
                form.getForm().findField("nome").setValue("");
                form.getForm().findField("tipoContribuinte").setValue("");
                form.getForm().findField("cpfcnpj").setValue("");
                
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaProduto: function(form, registro) {                
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/estoques/produtos/vasilhames/cadastro/exists/produto/vasilhame",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idProduto"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {                                        
                form.getForm().findField("descricaoProduto").setValue(resposta.registro.vasilhame.descricao);
                form.getForm().findField("tipoGarrafeira").setValue(resposta.registro.tipoGarrafeira);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "produtoVasilhame") {
                            form.getForm().findField("idProduto").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("descricaoProduto").setValue("");
                form.getForm().findField("tipoGarrafeira").setValue("0");
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaUsuario: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/cadastro/exists/usuario",
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
            url: BACKEND + "/vasilhame/entregues/itens/cadastro/exists",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form,
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {                               
                form.getForm().setValues({                                        
                    quantidadeBaixada: resposta.registro.quantidadeBaixada,                    
                    quantidadeBaixar: resposta.registro.quantidadeBaixar                                        
                });               
                
                if (form.lastSubmit) {
                    if (form.lastSubmit.quantidade != registro.quantidade) {
                        // Responsavel por recarregar o registro pai
                        loadParents({component: form});
                    }
                } else {
                    // Responsavel por recarregar o registro pai
                    loadParents({component: form});                    
                }
                
                me.filtros(form, resposta.registro.idEmpresa, resposta.registro.empresa.nome, 
                    resposta.registro.idFilial, resposta.registro.filial.descricao, resposta.registro.idCodigo,
                    resposta.registro.idProduto + resposta.registro.produto.digito, resposta.registro.produto.descricao,
                    resposta.registro.quantidadeBaixar, resposta.registro.quantidadeBaixada, resposta.registro.quantidade);                 
            }
        };
        
        requestServerJson(request);
    },
    
    filtros: function (form, idEmpresa, nomeEmpresa, idFilial, descricaoFilial, idVasilhameEntregueItem,
            idProduto, descricaoProduto, quantidadeBaixar, quantidadeBaixada, quantidade) {   
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
            field: "idVasilhameEntregueItem",
            fieldBtn: "conVasilhamesItens", 
            value: idVasilhameEntregueItem,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        }); 
        filtrosItens.push({
            field: "idProduto", 
            value: idProduto,
            readOnly: true,
            filterable: false
        }); 
        filtrosItens.push({
            field: "descricaoProduto", 
            value: descricaoProduto,
            readOnly: true,
            filterable: false
        }); 
        filtrosItens.push({
            field: "quantidade", 
            value: quantidadeBaixar,            
            readOnly: false,
            filterable: false
        }); 
        filtrosItens.push({
            field: "quantidadeBaixada", 
            value: quantidadeBaixada,            
            readOnly: false,
            filterable: false
        });
        filtrosItens.push({
            field: "quantidadeEntregue", 
            value: quantidade,            
            readOnly: false,
            filterable: false
        });
               
        if (form.modoCompl == "abas") {            
            var gridBaixa = form.down("entregueItemBaixaConGrid");            
            // Pega o parametro e envia para o registro filho
            
            // Recupera todos os dados do form para depois que fizer o recalcular fazer o exists
            var chave = {
                idEmpresa: form.getForm().findField("idEmpresa").getValue(),
                idFilial: form.getForm().findField("idFilial").getValue(),
                idCodigo: form.getForm().findField("idCodigo").getValue()
            };
            gridBaixa.params.loadParents = {
                controller: me,
                funcao: "carregaRegistrosLoadParents",
                params: [form, chave]
            };                         
            
            gridBaixa.params.fieldsFix = filtrosItens;
            setFilterFix(gridBaixa);
            if (!form.down("edicoesTopBar button[name=btnExcluir]").disabled) {
                if (form.down("entregueItemCadTabPanel").activeTab.xtype == "entregueItemCadBaixa") {
                    gridBaixa.store.currentPage = 1;
                    gridBaixa.store.load();
                }
            }
        }
        if (form.modoCompl == "botoes") {
            form.down("entregueItemCadButtonGroup").down("toolbar").down("button[name=baixa]").fieldsFix = filtrosItens;
        }
    }

});