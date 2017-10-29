Ext.define("App.controller.vasilhame.entregues.itens.baixas.notas.EntregueItemBaixaNotaRel", {
    extend: "Ext.app.Controller", 
    views: [
        "vasilhame.entregues.itens.baixas.notas.relatorio.Window",
        "vasilhame.entregues.itens.baixas.notas.relatorio.Form"
    ],
    
    init: function() {
        this.control ({
            "entregueItemBaixaNotaRelWindow": {
                show: this.onReady
            },
            
            "entregueItemBaixaNotaRelForm > relatoriosTopBar button[name=btnConfirmar]": {
                click: this.botaoTopBarConfirmar
            },
            "entregueItemBaixaNotaRelForm > relatoriosTopBar button[name=btnOrdenar]": {
                click: this.botaoTopBarOrdenar
            },
            "entregueItemBaixaNotaRelForm > relatoriosTopBar button[name=btnAjuda]": {
                click: this.botaoTopBarAjuda
            },
            "entregueItemBaixaNotaRelForm > relatoriosTopBar button[name=btnFechar]": {
                click: this.botaoTopBarFechar
            },
            "entregueItemBaixaNotaRelForm textfield[name=idEmpresaBaixa]": {
                focus: this.onFocus,
                blur: this.empresaBaixaBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conEmpresasBaixas]": {
                click: this.conEmpresasBaixasClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idFilialBaixa]": {
                focus: this.onFocus,
                blur: this.filialBaixaBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conFiliaisBaixas]": {
                click: this.conFiliaisBaixasClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idVasilhameEntregue]": {
                focus: this.onFocus,
                blur: this.vasilhameBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conVasilhames]": {
                click: this.conVasilhamesClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idPessoa]": {
                focus: this.onFocus,
                blur: this.pessoaBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conPessoas]": {
                click: this.conPessoasClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idUsuarioEntregue]": {
                focus: this.onFocus,
                blur: this.usuarioEntregueBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conUsuariosEntregue]": {
                click: this.conUsuariosEntregueClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idProduto]": {
                focus: this.onFocus,
                blur: this.produtoBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conProdutos]": {
                click: this.conProdutosClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idVasilhameEntregueItem]": {
                focus: this.onFocus,
                blur: this.vasilhameItemBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conVasilhamesItens]": {
                click: this.conVasilhamesItensClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idVasilhameEntregueItemBaixa]": {
                focus: this.onFocus,
                blur: this.vasilhameItemBaixaBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conVasilhamesItensBaixas]": {
                click: this.conVasilhamesItensBaixasClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idEmpresaNota]": {
                focus: this.onFocus,
                blur: this.empresaNotaBlur
            },
            "entregueItemBaixaNotaRelForm button[name=conEmpresasNotas]": {
                click: this.conEmpresasNotasClick
            },
            "entregueItemBaixaNotaRelForm textfield[name=idFilialNota]": {
                focus: this.onFocus,
                blur: this.filialNotaBlur,
                keydown: this.filialNotaKeyDown
            },
            "entregueItemBaixaNotaRelForm button[name=conFiliaisNotas]": {
                click: this.conFiliaisNotasClick
            }         
        });
    },

    onReady: function(window){
        var form = window.down("entregueItemBaixaNotaRelForm");
        var grid = form.down("grid[name=ordem]");
        
        grid.store.loadData([
            ["idEmpresaBaixa", "ASC", "Empresa da Baixa"],
            ["idFilialBaixa", "ASC", "Filial da Baixa"],
            ["idVasilhameEntregueItemBaixa", "ASC", "ID da Baixa"]
        ], false);            
            
        // Chamada para a função de foco
        var focusFirstOrForce = new FocusFirstOrForce();
        focusFirstOrForce.form = form;
        focusFirstOrForce.focus();
    },
    
    botaoTopBarConfirmar: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
        me.carregaRegistro(form, form.getForm().getValues());
    },
    
    botaoTopBarOrdenar: function(button) {
        var form = button.up("entregueItemBaixaNotaRelForm");
        var grid = form.down("grid[name=ordem]");
        if (!(grid.storeOrdCampos)) {
            grid.storeOrdCampos = Ext.create("App.store.Ordenar");
            grid.storeOrdCampos.dados = [
                ["idEmpresaNota", "ASC", "Empresa da Nota"],
                ["modelo", "ASC", "Modelo"],
                ["idFilialNota", "ASC", "Filial da Nota"],
                ["serie", "ASC", "Série"],
                ["numero", "ASC", "Número"],
                ["dataEmissao", "ASC", "Data Emissão"],
                ["sequencia", "ASC", "Sequência"]
            ];
            grid.storeOrdCampos.loadData([
                ["idEmpresaNota", "ASC", "Empresa da Nota"],
                ["modelo", "ASC", "Modelo"],
                ["idFilialNota", "ASC", "Filial da Nota"],
                ["serie", "ASC", "Série"],
                ["numero", "ASC", "Número"],
                ["dataEmissao", "ASC", "Data Emissão"],
                ["sequencia", "ASC", "Sequência"]
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
    
    empresaBaixaBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaNotaRelForm");
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
        var form = button.up("entregueItemBaixaNotaRelForm");
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
            var form = field.up("entregueItemBaixaNotaRelForm");
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
        var form = button.up("entregueItemBaixaNotaRelForm");
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
    
    vasilhameBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaNotaRelForm");
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
            me.carregaVasilhame(form, registro);
        }
    },

    conVasilhamesClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
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
    
    pessoaBlur: function (field) {
        if (field.getValue() != field.oldValue) {
            var me = this;            
            var form = field.up("entregueItemBaixaNotaRelForm");
            var idEmpresa = form.getForm().findField("idEmpresaBaixa").getValue();
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
        var form = button.up("entregueItemBaixaNotaRelForm");
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
    
    usuarioEntregueBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaNotaRelForm");
            if (field.getValue() == "" || field.getValue() == null) {
                form.getForm().findField("nomeUsuarioEntregue").setValue("");
                return false;
            }
            var registro = {
                login: field.getValue()
            };
            me.carregaUsuarioEntregue(form, registro);
        }
    },
    
    conUsuariosEntregueClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
        me.onFocus(form.getForm().findField("idUsuarioEntregue"));
        var campos = new Array();
        campos[0] = {
            tabela: "login",
            form: form.getForm().findField("idUsuarioEntregue")
        };
        var funcao = {
            controller: me,
            funcao: "usuarioEntregueBlur",
            parametros: [form.getForm().findField("idUsuarioEntregue")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idUsuarioEntregue")
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
            var form = field.up("entregueItemBaixaNotaRelForm");
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
        var form = button.up("entregueItemBaixaNotaRelForm");
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
            var form = field.up("entregueItemBaixaNotaRelForm");
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
            me.carregaVasilhameItem(form, registro);
        }
    },

    conVasilhamesItensClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
        me.onFocus(form.getForm().findField("idVasilhameEntregueItem"));
        var campos = new Array();
        campos[0] = {
            tabela: "idCodigo",
            form: form.getForm().findField("idVasilhameEntregueItem")
        };
        var funcao = {
            controller: me,
            funcao: "vasilhameItemBlur",
            parametros: [form.getForm().findField("idVasilhameEntregueItem")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idVasilhameEntregueItem")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemCon";
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
    
    vasilhameItemBaixaBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaNotaRelForm");
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
        var form = button.up("entregueItemBaixaNotaRelForm");
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
      
    empresaNotaBlur: function(field) {
        if (field.getValue() != field.oldValue) {
            var me = this;
            var form = field.up("entregueItemBaixaNotaRelForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("nomeEmpresaNota").setValue("");
                return false;
            }
            var registro = {
                id: field.getValue()
            };
            form.getForm().findField("idFilialNota").forceLoad = true;
            me.carregaEmpresaNota(form, registro);
        }
    },
    
    conEmpresasNotasClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
        me.onFocus(form.getForm().findField("idEmpresaNota"));
        var campos = new Array();
        campos[0] = {
            tabela: "id",
            form: form.getForm().findField("idEmpresaNota")
        };
        var funcao = {
            controller: me,
            funcao: "empresaNotaBlur",
            parametros: [form.getForm().findField("idEmpresaNota")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idEmpresaNota")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "geempr200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;
        callProgram.call();
    },

    filialNotaBlur: function(field) {
        if (field.getValue() != field.oldValue || field.forceLoad) {
            var me = this;
            field.forceLoad = false;
            var form = field.up("entregueItemBaixaNotaRelForm");
            if ( (isNaN(field.getValue())) || (field.getValue() == "") || (field.getValue() == 0) || (field.getValue() == null) ) {
                form.getForm().findField("descricaoFilialNota").setValue("");
                return false;
            }
            var idEmpresa = form.getForm().findField("idEmpresaNota").getValue();
            if (idEmpresa == "") {
                idEmpresa = 0;
            }
            
            var registro = {
                idEmpresa: idEmpresa,
                codigo: field.getValue()
            };
            me.carregaFilialNota(form, registro);
        }
    },

    conFiliaisNotasClick: function(button) {
        var me = this;
        var form = button.up("entregueItemBaixaNotaRelForm");
        me.onFocus(form.getForm().findField("idFilialNota"));
        var campos = new Array();
        campos[0] = {
            tabela: "codigo",
            form: form.getForm().findField("idFilialNota")
        };
        var funcao = {
            controller: me,
            funcao: "filialNotaBlur",
            parametros: [form.getForm().findField("idFilialNota")]
        };
        var destinos = {
            funcao: funcao,
            campos: campos,
            focus: form.getForm().findField("idFilialNota")
        };
        
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "gefili200_";
        callProgram.multi = "SINGLE";
        callProgram.destinos = destinos;        
        var idEmpresa = form.getForm().findField("idEmpresaNota").getValue();
        if ( (isNaN(idEmpresa)) || (idEmpresa == null) || (idEmpresa == "") ) {
            idEmpresa = 0;
        }
        var nomeEmpresa = form.getForm().findField("nomeEmpresaNota").getValue();    
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
    
    filialNotaKeyDown: function(field, e) {
        var me = this;
        if (!e.hasModifier()) {
            if (e.getKey() === e.TAB) {            
                var form = field.up("entregueItemBaixaNotaRelForm");
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
                url = BACKEND + "/vasilhame/entregues/itens/baixas/notas/relatorio/pdf";
            } else if (formato == "TXT") {
                url = BACKEND + "/vasilhame/entregues/itens/baixas/notas/relatorio/txt";
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
            callReport.nameFile = "Notas Fiscais das Baixas dos Itens dos Vasilhames Entregues." + formato.toLowerCase();
            callReport.records = registros;            
            callReport.sort = sort; 
            callReport.delimiter = RELATORIOSTXTDELIMITADOR;
            callReport.breakLine = RELATORIOSTXTQUEBRALINHA;                 
            callReport.call();
      
        }
    },
    
    carregaEmpresaBaixa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/exists/empresa/baixa",
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
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/exists/filial/baixa",
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
//                form.getForm().findField("descricaoFilial").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idCodigo") {
                            form.getForm().findField("idVasilhameEntregue").markInvalid(resposta.errors[i].msg);
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
    
    carregaUsuarioEntregue: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/cadastro/exists/usuario",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idUsuarioEntregue"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeUsuarioEntregue").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                form.getForm().findField("nomeUsuarioEntregue").setValue("");
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
            url: BACKEND + "/vasilhame/entregues/itens/baixas/cadastro/exists/item",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idVasilhameEntregueItem"),
            waitMsg: LOADMSG,
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "idCodigo") {
                            form.getForm().findField("idVasilhameEntregueItem").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
            }           
        };
        
        requestServerJson(request);
    },
    
    carregaVasilhameItemBaixa: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/exists/baixa",
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
    
    carregaEmpresaNota: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/exists/empresa/nota",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idEmpresaNota"),
            waitMsg: LOADMSG,
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("nomeEmpresaNota").setValue(resposta.registro.nome);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "id") {
                            form.getForm().findField("idEmpresaNota").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("nomeEmpresaNota").setValue("");
            }
        };
        
        requestServerJson(request);
    },
    
    carregaFilialNota: function(form, registro) {
        var request = {
            async: true,
            showMsgErrors: false,
            url: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/exists/filial/nota",
            parametros: Ext.JSON.encode({
                registro: registro
            }),
            component: form.getForm().findField("idFilialNota"),
            waitMsg: LOADMSG,            
            afterMsgSuccessTrue: function(resposta, request) {
                form.getForm().findField("descricaoFilialNota").setValue(resposta.registro.descricao);
            },
            afterMsgSuccessFalse: function(resposta, request) {
                if (resposta.errors) {
                    for (var i in resposta.errors) {
                        if (resposta.errors[i].id == "codigo") {
                            form.getForm().findField("idFilialNota").markInvalid(resposta.errors[i].msg);
                        }
                    }
                }
                form.getForm().findField("descricaoFilialNota").setValue("");
            }           
        };
        
        requestServerJson(request);
    }

});