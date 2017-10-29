Ext.define("App.view.vasilhame.entregar.itens.cadastro.Form", {
    extend: "App.util.EdicoesForm", 
    alias : "widget.entregarItemCadForm",
    
    requires: [
        "App.util.EdicoesForm",
        "App.util.mascaras.NumericField",
        "App.util.mascaras.ComboDefaultField",
        "App.util.mascaras.CPFsCNPJsField",
        "App.util.mascaras.ComboDigitoField"
    ],
    
    modoCompl: "abas",
    
    initComponent: function() {
        var me = this;
        Ext.apply(me.fieldDefaults, {
            labelWidth: 135,
            considerGetValues: true
        });
        
        var conEmpresas = clone(formButtonConsultar);
        conEmpresas.name = "conEmpresas";
        conEmpresas.tooltip = "Consulta de Empresas";
        conEmpresas.disabled = EMPRESADESABILITAR100;
        
        var conFiliais = clone(formButtonConsultar);
        conFiliais.name = "conFiliais";
        conFiliais.tooltip = "Consulta de Filiais";
             
        var conVasilhames = clone(formButtonConsultar);
        conVasilhames.name = "conVasilhames";
        conVasilhames.tooltip = "Consulta de Vasilhames";
             
        var conPessoas = clone(formButtonConsultar);
        conPessoas.name = "conPessoas";
        conPessoas.tooltip = "Consulta de Pessoas";
        conPessoas.disabled = true;
        
        var conProdutos = clone(formButtonConsultar);
        conProdutos.name = "conProdutos";
        conProdutos.tooltip = "Consulta de Produtos";
        
        var conUsuarios = clone(formButtonConsultar);
        conUsuarios.name = "conUsuarios";
        conUsuarios.tooltip = "Consulta Usuários";
        conUsuarios.disabled = true;
                
        var itemsFormCampos = [
            {
                xtype: "container",
                name: "containerCampos",
                flex: 1,
                autoScroll: true,
                layout: "anchor",
                items: [
                    {
                        xtype: "fieldcontainer",
                        fieldLabel: "<b>Vasilhame</b>",
                        anchor: "100%",                        
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults,
                        items: [
                            {
                                xtype: "combodefaultfield",
                                name: "idVasilhameEntregar",
                                allowBlank: false,
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
                                    },{
                                        field: "pessoa.codigoDigito",
                                        fieldSetValue: "idPessoa"
                                    },{
                                        field: "nome",
                                        fieldSetValue: "nome"
                                    },{
                                        field: "tipoContribuinte",
                                        fieldSetValue: "tipoContribuinte"
                                    },{
                                        field: "cpfcnpj",
                                        fieldSetValue: "cpfcnpj"
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
                        fieldLabel: "<b>Pessoa</b>",
                        anchor: "100%",
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults,
                        items: [
                            {
                                xtype: "combodigitofield",
                                name: "idPessoa",
                                readOnly: true,
                                considerGetValues: false,
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
                                    },{
                                        field: "situacao_gepess",
                                        value: "1",
                                        type: "string",
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
                                considerGetValues: false,
                                readOnly: true,                                
                                flex: 1
                            }
                        ]            
                    },
                    {
                        xtype: "combo",
                        fieldLabel: "<b>Contribuinte</b>",
                        name: "tipoContribuinte",                
                        width: 270,
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                ["1", "1 - Pessoa Física"],
                                ["2", "2 - Pessoa Jurídica"]                
                            ]
                        }),
                        queryMode: "local",
                        value: "1",
                        valueField: "value",
                        displayField: "text",
                        considerGetValues: false,
                        readOnly: true,                        
                        editable: false 
                    },   
                    {
                        xtype: "cpfscnpjsfield",
                        fieldLabel: "CPF/CNPJ",
                        width: 280,
                        name: "cpfcnpj",
                        considerGetValues: false,
                        readOnly: true,
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults         
                    },
                    {
                        xtype: "fieldcontainer",
                        fieldLabel: "<b>Produto</b>",
                        anchor: "100%",
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults,
                        items: [
                            {
                                xtype: "combodigitofield",
                                name: "idProduto",
                                allowBlank: false,
                                pageSize: 0,
                                store: createStoreCombobox({
                                    store: "App.store.estoque.produtos.vasilhames.VasilhamesDistinct",
                                    form: this,
                                    filterEncode: false,
                                    fieldAutoComplete: "idProduto",                            
                                    sorters: [{property: "vasilhame.descricao", direction: CONSULTASORDENAR}],
                                    fieldsFilter: [{
                                        grupo: [{
                                            field: "idVasilhame",
                                            type: "int",
                                            comparison: "eq",
                                            connector: "AND",
                                            codigo: true
                                        },{
                                            field: "vasilhame.digito",
                                            type: "string",
                                            comparison: "eq",
                                            connector: "AND",
                                            digito: true
                                        }]
                                    },{
                                        field: "vasilhame.descricao",
                                        type: "string",
                                        comparison: "bw"
                                    }],                                    
                                    useFieldSetOldValue: "codigoDigito",
                                    fieldsSetValues: [{
                                        field: "codigoDigito",
                                        fieldSetValue: "idProduto"
                                    },{
                                        field: "vasilhame.descricao",
                                        fieldSetValue: "descricaoProduto"                                    
                                    },{
                                        field: "tipoGarrafeira",
                                        fieldSetValue: "tipoGarrafeira"                                    
                                    }]
                                }),
                                valueField: "codigoDigito",
                                displayField: "codigoDigito",
                                tpl: Ext.create('Ext.XTemplate',
                                    '<ul class="x-list-plain"><tpl for=".">',
                                        '<li role="option" class="x-boundlist-item">{idVasilhame}-{vasilhame.digito} - {vasilhame.descricao}</li>',
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
                        xtype: "fieldcontainer",
                        anchor: "100%",
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults,
                        items:[
                            {
                                xtype: "fieldcontainer",
                                flex: 1,
                                layout: "hbox",
                                autoScroll: true,
                                fieldDefaults: formFieldDefaults,
                                items:[
                                    {
                                        xtype: "numericfield",
                                        fieldLabel: "<b>Quantidade</b>",
                                        name: "quantidade",
                                        allowBlank: false,
                                        width: 250,
                                        decimalPrecision: 3
                                    },
                                    {
                                        xtype: "numericfield",
                                        fieldLabel: "Quantidade Baixada",
                                        name: "quantidadeBaixada",
                                        readOnly: true,
                                        considerGetValues: false,
                                        width: 250,
                                        decimalPrecision: 3
                                    },
                                    {
                                        xtype: "numericfield",
                                        fieldLabel: "Quantidade a Baixar",
                                        name: "quantidadeBaixar",
                                        readOnly: true,
                                        considerGetValues: false,
                                        width: 250,
                                        decimalPrecision: 3
                                    }
                                ]
                            }
                        ]
                    },                       
                    {
                        xtype: "combo",
                        fieldLabel: "<b>Garrafeira</b>",
                        name: "tipoGarrafeira",                
                        width: 240,
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
                        value: "0",
                        displayField: "text",
                        readOnly: true,
                        editable: false,
                        allowBlank: false
                    },
                    {
                        xtype: "combo",
                        fieldLabel: "<b>Situação</b>",
                        enableKeyEvents: true,
                        name: "situacao",                
                        width: 240,
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
                        value: "1",
                        displayField: "text",
                        editable: false,
                        allowBlank: false
                    },            
                    {
                        xtype: "datefield",
                        fieldLabel: "Data/Hora Gravação",
                        name: "dataHoraGravacao",
                        readOnly: true,
                        format: "d/m/Y H:i:s",
                        submitFormat: "Y-m-d H:i:s",
                        width: 280
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
                                readOnly: true,
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
                ]
            }
        ];
        
        if (me.modoCompl == "abas") {
            itemsFormCampos.push({
                xtype: "splitter",
                collapsible: true
            });            
            itemsFormCampos.push({                        
                xtype: "entregarItemCadTabPanel",
                flex: 1,
                title: TABPANELTITLE,
                collapsible: true,
                collapseDirection: "bottom"
            });
        }
        if (me.modoCompl == "botoes") {                       
            itemsFormCampos.push({
                xtype: "entregarItemCadButtonGroup",
                title: TABPANELTITLE
            });
        }
        
        var itemsForm = [
            {
                xtype: "fieldcontainer",
                fieldLabel: "<b>Empresa</b>",
                anchor: "100%",
                layout: "hbox",
                hidden: EMPRESAOCULTAR100,
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idEmpresa",
                        allowBlank: false,
                        value: EMPRESACOD,
                        readOnly: EMPRESADESABILITAR100,
                        store: createStoreCombobox({
                            store: "App.store.geral.geempr200_.Empresas",
                            form: this,
                            filterEncode: false,
                            noExecuteBlur: false,
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
                fieldLabel: "<b>Filial</b>",
                anchor: "100%",
                layout: "hbox",
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idFilial",
                        allowBlank: false,
                        store: createStoreCombobox({
                            store: "App.store.geral.gefili200_.Filiais",
                            form: this,
                            filterEncode: false,
                            noExecuteBlur: false,
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
                xtype: "numericfield",
                fieldLabel: "<b>ID</b>",
                name: "idCodigo",
                width: 225,
                readOnly: true
            },
            {
                html: "<hr />",
                frame: true,
                style: {
                    border: 0,
                    height: 5
                }            
            },
            {
                xtype: "container",
                anchor: "100% -110",
                layout: {
                    type: "vbox",
                    align: "stretch"
                }, 
                items: itemsFormCampos
            }
        ];
        me.items = itemsForm;
        me.callParent(arguments);
        
        setFieldsFix(me);
    }
});