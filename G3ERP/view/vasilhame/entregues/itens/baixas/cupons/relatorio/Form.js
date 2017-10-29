Ext.define("App.view.vasilhame.entregues.itens.baixas.cupons.relatorio.Form", {
    extend: "App.util.RelatoriosForm", 
    alias : "widget.entregueItemBaixaCupomRelForm",

    requires: [
        "App.util.RelatoriosForm",
        "App.util.RelatoriosGridOrdem",
        "App.util.mascaras.ComboDefaultField",
        "App.util.mascaras.ComboDigitoField"
    ],

    initComponent: function() {
        var me = this;
        Ext.apply(me.fieldDefaults, {
            labelWidth: 100,
            considerGetValues: true
        });
        
        var conEmpresasBaixas = clone(formButtonConsultar);
        conEmpresasBaixas.name = "conEmpresasBaixas";
        conEmpresasBaixas.tooltip = "Consulta de Empresas";
        conEmpresasBaixas.disabled = EMPRESADESABILITAR100;
        
        var conFiliaisBaixas = clone(formButtonConsultar);
        conFiliaisBaixas.name = "conFiliaisBaixas";
        conFiliaisBaixas.tooltip = "Consulta de Filiais";
             
        var conVasilhames = clone(formButtonConsultar);
        conVasilhames.name = "conVasilhames";
        conVasilhames.tooltip = "Consulta de Vasilhames";
        
        var conPessoas = clone(formButtonConsultar);
        conPessoas.name = "conPessoas";
        conPessoas.tooltip = "Consulta de Pessoas";
        
        var conUsuariosEntregue = clone(formButtonConsultar);
        conUsuariosEntregue.name = "conUsuariosEntregue";
        conUsuariosEntregue.tooltip = "Consulta Usuários";
        
        var conVasilhamesItens = clone(formButtonConsultar);
        conVasilhamesItens.name = "conVasilhamesItens";
        conVasilhamesItens.tooltip = "Consulta de Vasilhames Itens";
        
        var conProdutos = clone(formButtonConsultar);
        conProdutos.name = "conProdutos";
        conProdutos.tooltip = "Consulta de Produtos";
             
        var conVasilhamesItensBaixas = clone(formButtonConsultar);
        conVasilhamesItensBaixas.name = "conVasilhamesItensBaixas";
        conVasilhamesItensBaixas.tooltip = "Consulta de Vasilhames dos Itens das Baixas";
        
        var conEmpresasCupons = clone(formButtonConsultar);
        conEmpresasCupons.name = "conEmpresasCupons";
        conEmpresasCupons.tooltip = "Consulta de Empresas";
        conEmpresasCupons.disabled = EMPRESADESABILITAR100;
        
        var conFiliaisCupons = clone(formButtonConsultar);
        conFiliaisCupons.name = "conFiliaisCupons";
        conFiliaisCupons.tooltip = "Consulta de Filiais";
        
        var conECFs = clone(formButtonConsultar);
        conECFs.name = "conECFs";
        conECFs.tooltip = "Consulta ECFs";
        
        me.items = [
            {
                xtype: "relatoriosGridOrdem",
                store: Ext.create("App.store.Ordenar")
            },
            {
                xtype: "tbspacer",
                height: 10
            },           
            {
                xtype: "combo",
                fieldLabel: "<b>Formato</b>",
                name: "formato",
                anchor: "100%",
                store: Ext.create("App.store.RelatoriosFormato"),
                queryMode: "local",
                value: RELATORIOSFORMATO,
                valueField: "value",
                displayField: "text",
                editable: false
            },
            {
                xtype: "combo",
                fieldLabel: "<b>Zebrado</b>",
                name: "zebrado",
                anchor: "100% ",
                store: Ext.create("App.store.RelatoriosZebrado"),
                queryMode: "local",
                value: RELATORIOSZEBRADO,
                valueField: "value",
                displayField: "text",
                editable: false                
            },
            {
                xtype: "fieldcontainer",
                anchor: "100%",     
                items:[
                    {
                        xtype: "fieldset",
                        defaultType: "textfield",
                        title: "Dados dos Vasilhames",
                        anchor: "100%",   
                        items: [
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Empresa",
                                anchor: "100%",
                                layout: "hbox",
                                hidden: EMPRESAOCULTAR300,
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idEmpresaBaixa",
                                        allowComma: true,
                                        value: EMPRESACOD,
                                        readOnly: EMPRESADESABILITAR300,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.geempr200_.Empresas",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idEmpresaBaixa",
                                            sorters: [{property: "nome", direction: CONSULTASORDENAR}],
                                            fieldsFilter: [{
                                                field: "id",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "nome",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "id",
                                            fieldsSetValues: [{
                                                field: "id",
                                                fieldSetValue: "idEmpresaBaixa"
                                            },{
                                                field: "nome",
                                                fieldSetValue: "nomeEmpresaBaixa"
                                            }]
                                        }),
                                        valueField: "idEmpresaBaixa",
                                        displayField: "idEmpresaBaixa",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{id} - {nome}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conEmpresasBaixas,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "nomeEmpresaBaixa",
                                        value: EMPRESANOM,
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Filial",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idFilialBaixa",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.gefili200_.Filiais",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idFilialBaixa",
                                            sorters: [{property: "descricao", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "codigo",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "descricao",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "codigo",
                                            fieldsSetValues: [{
                                                field: "codigo",
                                                fieldSetValue: "idFilialBaixa"
                                            },{
                                                field: "descricao",
                                                fieldSetValue: "descricaoFilialBaixa"
                                            }]
                                        }),
                                        valueField: "idFilialBaixa",
                                        displayField: "idFilialBaixa",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{codigo} - {descricao}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conFiliaisBaixas,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "descricaoFilialBaixa",
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Vasilhame",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idVasilhameEntregue",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.vasilhame.entregues.Entregues",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idVasilhameEntregue",
                                            sorters: [{property: "idCodigo", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "idFilial",
                                                value: 0,
                                                getValueFieldForm: "idFilialBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "idCodigo",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            useFieldSetOldValue: "idCodigo",
                                            fieldsSetValues: [{
                                                field: "idCodigo",
                                                fieldSetValue: "idVasilhameEntregue"
                                            }]
                                        }),
                                        valueField: "idVasilhameEntregue",
                                        displayField: "idVasilhameEntregue",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{idCodigo}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conVasilhames
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Pessoa",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodigitofield",
                                        name: "idPessoa",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.gepess200.Pessoas",
                                            form: this,
                                            fieldAutoComplete: "idPessoa",                            
                                            sorters: [{property: "nome_gepess", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "empresa_gepess",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                grupo: [{
                                                    field: "codigo_gepess",
                                                    type: "int",
                                                    comparison: "eq",
                                                    connector: "AND",
                                                    codigo: true
                                                },{
                                                    field: "digito_gepess",
                                                    type: "string",
                                                    comparison: "eq",
                                                    connector: "AND",
                                                    digito: true
                                                }]
                                            },{
                                                field: "nome_gepess",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "codigoDigito",
                                            fieldsSetValues: [{
                                                field: "codigoDigito",
                                                fieldSetValue: "idPessoa"
                                            },{
                                                field: "nome_gepess",
                                                fieldSetValue: "nome"
                                            }]
                                        }),
                                        valueField: "idPessoa",
                                        displayField: "idPessoa",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{codigo_gepess}-{digito_gepess} - {nome_gepess}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conPessoas,
                                    {
                                        xtype: "splitter"
                                    },
                                    {
                                        xtype: "textfield",
                                        name: "nome",
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Situação Vasilhame",
                                anchor: "100%",                        
                                items:[
                                    {
                                        xtype: "fieldset",
                                        defaultType: "checkboxfield",
                                        anchor: "100%",                        
                                        items: [
                                            {
                                                xtype: "checkboxgroup",
                                                columns: 4,
                                                vertical: true,
                                                items: [
                                                    { 
                                                        boxLabel: "Aberto", 
                                                        name: "aberto", 
                                                        inputValue: "1",
                                                        width: 300
                                                    },
                                                    { 
                                                        boxLabel: "Pendente", 
                                                        name: "pendente", 
                                                        inputValue: "2", 
                                                        width: 300
                                                    },                              
                                                    { 
                                                        boxLabel: "Inutilizado", 
                                                        name: "inutilizado", 
                                                        inputValue: "8", 
                                                        width: 200
                                                    },
                                                    {
                                                        boxLabel: "Fechado",
                                                        name: "fechado",
                                                        inputValue: "9",
                                                        width: 200
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Usuário Entregue",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idUsuarioEntregue",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.sistema.siusua200_.Usuarios",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idUsuarioEntregue",
                                            sorters: [{property: "login", direction: CONSULTASORDENAR}],
                                            fieldsFilter: [{
                                                field: "login",
                                                type: "string",
                                                comparison: "bw"
                                            },{
                                                field: "nome",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "login",
                                            fieldsSetValues: [{
                                                field: "login",
                                                fieldSetValue: "idUsuarioEntregue"
                                            },{
                                                field: "nome",
                                                fieldSetValue: "nomeUsuarioEntregue"
                                            }]
                                        }),
                                        valueField: "idUsuarioEntregue",
                                        displayField: "idUsuarioEntregue",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{login} - {nome}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conUsuariosEntregue,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "nomeUsuarioEntregue",
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Vasilhame Item",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idVasilhameEntregueItem",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.vasilhame.entregues.itens.Itens",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idVasilhameEntregueItem",
                                            sorters: [{property: "idCodigo", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "idFilial",
                                                value: 0,
                                                getValueFieldForm: "idFilialBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "idCodigo",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            useFieldSetOldValue: "idCodigo",
                                            fieldsSetValues: [{
                                                field: "idCodigo",
                                                fieldSetValue: "idVasilhameEntregueItem"
                                            }]
                                        }),
                                        valueField: "idVasilhameEntregueItem",
                                        displayField: "idVasilhameEntregueItem",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{idCodigo}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conVasilhamesItens
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Produto",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodigitofield",
                                        name: "idProduto",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.estoque.esprod200.Produtos",
                                            form: this,
                                            fieldAutoComplete: "idProduto",                            
                                            sorters: [{property: "descricao_esprod", direction: CONSULTASORDENAR}],
                                            fieldsFilter: [{
                                                grupo: [{
                                                    field: "codigo_esprod",
                                                    type: "int",
                                                    comparison: "eq",
                                                    connector: "AND",
                                                    codigo: true
                                                },{
                                                    field: "digito_esprod",
                                                    type: "string",
                                                    comparison: "eq",
                                                    connector: "AND",
                                                    digito: true
                                                }]
                                            },{
                                                field: "descricao_esprod",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "codigoDigito",
                                            fieldsSetValues: [{
                                                field: "codigoDigito",
                                                fieldSetValue: "idProduto"
                                            },{
                                                field: "descricao_esprod",
                                                fieldSetValue: "descricaoProduto"
                                            }]
                                        }),
                                        valueField: "idProduto",
                                        displayField: "idProduto",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{codigo_esprod}-{digito_esprod} - {descricao_esprod}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conProdutos,
                                    {
                                        xtype: "splitter"
                                    },
                                    {
                                        xtype: "textfield",
                                        name: "descricaoProduto",
                                        readOnly: true,
                                        considerGetValues: false,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "combo",
                                fieldLabel: "Garrafeira",
                                name: "garrafeira",
                                width: 260,
                                store: Ext.create("Ext.data.ArrayStore" ,{
                                    fields: ["value", "text"],
                                    data: [
                                            [null, "Selecione"],
                                            ["0", "0 - Não"],    
                                            ["1", "1 - Sim"]
                                    ]
                                }),
                                queryMode: "local",
                                emptyText: "Selecione",
                                valueField: "value",
                                displayField: "text",
                                editable: false                                          
                            },
                            {
                                xtype: "combo",
                                fieldLabel: "Situação Item",
                                name: "situacao",
                                width: 260,
                                store: Ext.create("Ext.data.ArrayStore" ,{
                                    fields: ["value", "text"],
                                    data: [
                                            [null, "Selecione"],
                                            ["1", "1 - Normal"],    
                                            ["2", "2 - Cancelado"]
                                    ]
                                }),
                                queryMode: "local",
                                emptyText: "Selecione",
                                valueField: "value",
                                displayField: "text",
                                editable: false                                          
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "ID Baixa",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idVasilhameEntregueItemBaixa",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.vasilhame.entregues.itens.baixas.Baixas",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idVasilhameEntregueItemBaixa",
                                            sorters: [{property: "idCodigo", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "idFilial",
                                                value: 0,
                                                getValueFieldForm: "idFilialBaixa",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "idCodigo",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            useFieldSetOldValue: "idCodigo",
                                            fieldsSetValues: [{
                                                field: "idCodigo",
                                                fieldSetValue: "idVasilhameEntregueItemBaixa"
                                            }]
                                        }),
                                        valueField: "idVasilhameEntregueItemBaixa",
                                        displayField: "idVasilhameEntregueItemBaixa",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{idCodigo}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conVasilhamesItensBaixas
                                ]            
                            },
                            {
                                xtype: "combo",
                                fieldLabel: "Tipo Baixa",
                                name: "tipoBaixa",
                                width: 260,
                                store: Ext.create("Ext.data.ArrayStore" ,{
                                    fields: ["value", "text"],
                                    data: [
                                            [null, "Selecione"],
                                            ["1", "1 - Venda"],    
                                            ["2", "2 - Devolução"]
                                    ]
                                }),
                                queryMode: "local",
                                emptyText: "Selecione",
                                valueField: "value",
                                displayField: "text",
                                editable: false                                          
                            }
                        ]
                    }
                ]
            },
            {
                xtype: "fieldcontainer",
                anchor: "100%",     
                items:[
                    {
                        xtype: "fieldset",
                        defaultType: "textfield",
                        title: "Dados do Cupom Fiscal",
                        anchor: "100%",   
                        items: [
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Empresa",
                                anchor: "100%",
                                layout: "hbox",
                                hidden: EMPRESAOCULTAR300,
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idEmpresaCupom",
                                        allowComma: true,
                                        value: EMPRESACOD,
                                        readOnly: EMPRESADESABILITAR300,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.geempr200_.Empresas",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idEmpresaCupom",
                                            sorters: [{property: "nome", direction: CONSULTASORDENAR}],
                                            fieldsFilter: [{
                                                field: "id",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "nome",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "id",
                                            fieldsSetValues: [{
                                                field: "id",
                                                fieldSetValue: "idEmpresaCupom"
                                            },{
                                                field: "nome",
                                                fieldSetValue: "nomeEmpresaCupom"
                                            }]
                                        }),
                                        valueField: "idEmpresaCupom",
                                        displayField: "idEmpresaCupom",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{id} - {nome}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conEmpresasCupons,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "nomeEmpresaCupom",
                                        value: EMPRESANOM,
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "Filial",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idFilialCupom",
                                        allowComma: true,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.gefili200_.Filiais",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idFilialCupom",
                                            sorters: [{property: "descricao", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaCupom",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "codigo",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "descricao",
                                                type: "string",
                                                comparison: "bw"
                                            }],
                                            useFieldSetOldValue: "codigo",
                                            fieldsSetValues: [{
                                                field: "codigo",
                                                fieldSetValue: "idFilialCupom"
                                            },{
                                                field: "descricao",
                                                fieldSetValue: "descricaoFilialCupom"
                                            }]
                                        }),
                                        valueField: "idFilialCupom",
                                        displayField: "idFilialCupom",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{codigo} - {descricao}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conFiliaisCupons,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "descricaoFilialCupom",
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "fieldcontainer",
                                fieldLabel: "ECF",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idEcfCupom",
                                        allowComma: true,
                                        enableKeyEvents: true,
                                        store: createStoreCombobox({
                                            store: "App.store.pdv.pdecfs200.ECFs",
                                            form: this,
                                            filterEncode: true,                            
                                            fieldAutoComplete: "idEcfCupom",
                                            sorters: [{property: "ecf_pdecfs", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "empresa_pdecfs",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaCupom",
                                                type: "int",
                                                comparison: "eq"
                                            },{
                                                field: "filial_pdecfs",
                                                value: 0,
                                                getValueFieldForm: "idFilialCupom",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            fieldsFilter: [{
                                                field: "ecf_pdecfs",
                                                type: "int",
                                                comparison: "eq"
                                            }],
                                            useFieldSetOldValue: "ecf_pdecfs",
                                            fieldsSetValues: [{
                                                field: "ecf_pdecfs",
                                                fieldSetValue: "idEcfCupom"
                                            }]
                                        }),
                                        valueField: "idEcfCupom",
                                        displayField: "idEcfCupom",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{ecf_pdecfs}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conECFs
                                ]            
                            }
                        ]
                    }
                ]
            }
        ];
        me.callParent(arguments);
    }
});
