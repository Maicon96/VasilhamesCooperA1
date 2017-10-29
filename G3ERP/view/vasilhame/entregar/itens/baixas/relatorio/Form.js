Ext.define("App.view.vasilhame.entregar.itens.baixas.relatorio.Form", {
    extend: "App.util.RelatoriosForm", 
    alias : "widget.entregarItemBaixaRelForm",

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
        
        var conEmpresas = clone(formButtonConsultar);
        conEmpresas.name = "conEmpresas";
        conEmpresas.tooltip = "Consulta de Empresas";
        conEmpresas.disabled = EMPRESADESABILITAR300;
        
        var conFiliais = clone(formButtonConsultar);
        conFiliais.name = "conFiliais";
        conFiliais.tooltip = "Consulta de Filiais";
        
        var conVasilhames = clone(formButtonConsultar);
        conVasilhames.name = "conVasilhames";
        conVasilhames.tooltip = "Consulta de Vasilhames";
        
        var conPessoas = clone(formButtonConsultar);
        conPessoas.name = "conPessoas";
        conPessoas.tooltip = "Consulta de Pessoas";
        
        var conUsuariosEntregar = clone(formButtonConsultar);
        conUsuariosEntregar.name = "conUsuariosEntregar";
        conUsuariosEntregar.tooltip = "Consulta Usuários";
        
        var conVasilhamesItens = clone(formButtonConsultar);
        conVasilhamesItens.name = "conVasilhamesItens";
        conVasilhamesItens.tooltip = "Consulta de Vasilhames Itens";
        
        var conProdutos = clone(formButtonConsultar);
        conProdutos.name = "conProdutos";
        conProdutos.tooltip = "Consulta de Produtos";
        
        var conUsuarios = clone(formButtonConsultar);
        conUsuarios.name = "conUsuarios";
        conUsuarios.tooltip = "Consulta Usuários";
        
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
                fieldLabel: "Empresa",
                anchor: "100%",
                layout: "hbox",
                hidden: EMPRESAOCULTAR300,
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idEmpresa",
                        allowComma: true,
                        value: EMPRESACOD,
                        readOnly: EMPRESADESABILITAR300,
                        store: createStoreCombobox({
                            store: "App.store.geral.geempr200_.Empresas",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idEmpresa",
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
                                fieldSetValue: "idEmpresa"
                            },{
                                field: "nome",
                                fieldSetValue: "nomeEmpresa"
                            }]
                        }),
                        valueField: "idEmpresa",
                        displayField: "idEmpresa",
                        tpl: Ext.create('Ext.XTemplate',
                            '<ul class="x-list-plain"><tpl for=".">',
                                '<li role="option" class="x-boundlist-item">{id} - {nome}</li>',
                            '</tpl></ul>'
                        ),
                        width: 85
                    },
                        conEmpresas,
                    {
                        xtype: "splitter"
                    },                    
                    {
                        xtype: "textfield",
                        name: "nomeEmpresa",
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
                        name: "idFilial",
                        allowComma: true,
                        store: createStoreCombobox({
                            store: "App.store.geral.gefili200_.Filiais",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idFilial",
                            sorters: [{property: "descricao", direction: CONSULTASORDENAR}],
                            fieldsFilterFix: [{
                                field: "idEmpresa",
                                value: 0,
                                getValueFieldForm: "idEmpresa",
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
                                fieldSetValue: "idFilial"
                            },{
                                field: "descricao",
                                fieldSetValue: "descricaoFilial"
                            }]
                        }),
                        valueField: "idFilial",
                        displayField: "idFilial",
                        tpl: Ext.create('Ext.XTemplate',
                            '<ul class="x-list-plain"><tpl for=".">',
                                '<li role="option" class="x-boundlist-item">{codigo} - {descricao}</li>',
                            '</tpl></ul>'
                        ),
                        width: 85
                    },
                        conFiliais,
                    {
                        xtype: "splitter"
                    },                    
                    {
                        xtype: "textfield",
                        name: "descricaoFilial",
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
                        name: "idVasilhameEntregar",
                        allowComma: true,
                        store: createStoreCombobox({
                            store: "App.store.vasilhame.entregar.Entregar",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idVasilhameEntregar",
                            sorters: [{property: "idCodigo", direction: CONSULTASORDENAR}],
                            fieldsFilterFix: [{
                                field: "idEmpresa",
                                value: 0,
                                getValueFieldForm: "idEmpresa",
                                type: "int",
                                comparison: "eq"
                            },{
                                field: "idFilial",
                                value: 0,
                                getValueFieldForm: "idFilial",
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
                                fieldSetValue: "idVasilhameEntregar"
                            }]
                        }),
                        valueField: "idVasilhameEntregar",
                        displayField: "idVasilhameEntregar",
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
                                getValueFieldForm: "idEmpresa",
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
                                columns: 2,
                                vertical: true,
                                items: [                                    
                                    { 
                                        boxLabel: "Pendente", 
                                        name: "pendente", 
                                        inputValue: "2", 
                                        width: 300
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
                fieldLabel: "Usuário Entregar",
                anchor: "100%",
                layout: "hbox",
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idUsuarioEntregar",
                        allowComma: true,
                        store: createStoreCombobox({
                            store: "App.store.sistema.siusua200_.Usuarios",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idUsuarioEntregar",
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
                                fieldSetValue: "idUsuarioEntregar"
                            },{
                                field: "nome",
                                fieldSetValue: "nomeUsuarioEntregar"
                            }]
                        }),
                        valueField: "idUsuarioEntregar",
                        displayField: "idUsuarioEntregar",
                        tpl: Ext.create('Ext.XTemplate',
                            '<ul class="x-list-plain"><tpl for=".">',
                                '<li role="option" class="x-boundlist-item">{login} - {nome}</li>',
                            '</tpl></ul>'
                        ),
                        width: 85
                    },
                        conUsuariosEntregar,
                    {
                        xtype: "splitter"
                    },                    
                    {
                        xtype: "textfield",
                        name: "nomeUsuarioEntregar",
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
                        name: "idVasilhameEntregarItem",
                        allowComma: true,
                        store: createStoreCombobox({
                            store: "App.store.vasilhame.entregar.itens.Itens",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idVasilhameEntregarItem",
                            sorters: [{property: "idCodigo", direction: CONSULTASORDENAR}],
                            fieldsFilterFix: [{
                                field: "idEmpresa",
                                value: 0,
                                getValueFieldForm: "idEmpresa",
                                type: "int",
                                comparison: "eq"
                            },{
                                field: "idFilial",
                                value: 0,
                                getValueFieldForm: "idFilial",
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
                                fieldSetValue: "idVasilhameEntregarItem"
                            }]
                        }),
                        valueField: "idVasilhameEntregarItem",
                        displayField: "idVasilhameEntregarItem",
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
                xtype: "combo",
                fieldLabel: "Tipo Baixa",
                name: "tipoBaixa",
                width: 260,
                store: Ext.create("Ext.data.ArrayStore" ,{
                    fields: ["value", "text"],
                    data: [
                            [null, "Selecione"],
                            ["1", "1 - Entrega"]                                
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
                fieldLabel: "Usuário",
                anchor: "100%",
                layout: "hbox",
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idUsuario",
                        allowComma: true,
                        enableKeyEvents: true,
                        store: createStoreCombobox({
                            store: "App.store.sistema.siusua200_.Usuarios",
                            form: this,
                            filterEncode: false,                            
                            fieldAutoComplete: "idUsuario",
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
                                fieldSetValue: "idUsuario"
                            },{
                                field: "nome",
                                fieldSetValue: "nomeUsuario"
                            }]
                        }),
                        valueField: "idUsuario",
                        displayField: "idUsuario",
                        tpl: Ext.create('Ext.XTemplate',
                            '<ul class="x-list-plain"><tpl for=".">',
                                '<li role="option" class="x-boundlist-item">{login} - {nome}</li>',
                            '</tpl></ul>'
                        ),
                        width: 85
                    },
                        conUsuarios,
                    {
                        xtype: "splitter"
                    },                    
                    {
                        xtype: "textfield",
                        name: "nomeUsuario",
                        considerGetValues: false,
                        readOnly: true,
                        flex: 1
                    }
                ]            
            }
        ];
        me.callParent(arguments);
    }
});
