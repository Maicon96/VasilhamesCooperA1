Ext.define("App.view.vasilhame.entregar.itens.baixas.cadastro.Form", {
    extend: "App.util.EdicoesForm", 
    alias : "widget.entregarItemBaixaCadForm",
    
    requires: [
        "App.util.EdicoesForm",
        "App.util.mascaras.NumericField",
        "App.util.mascaras.ComboDefaultField",
        "App.util.mascaras.ComboDigitoField"
    ],
    
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
             
        var conVasilhamesItens = clone(formButtonConsultar);
        conVasilhamesItens.name = "conVasilhamesItens";
        conVasilhamesItens.tooltip = "Consulta de Vasilhames Itens";
             
        var conProdutos = clone(formButtonConsultar);
        conProdutos.name = "conProdutos";
        conProdutos.tooltip = "Consulta de Produtos";
        conProdutos.disabled = true;
        
        var conUsuarios = clone(formButtonConsultar);
        conUsuarios.name = "conUsuarios";
        conUsuarios.tooltip = "Consulta Usuários";
        conUsuarios.disabled = true;
                
        me.items = [
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
                xtype: "fieldcontainer",
                fieldLabel: "Vasilhame Item",
                anchor: "100%",
                layout: "hbox",
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idVasilhameEntregarItem",
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
                fieldLabel: "<b>Produto</b>",
                anchor: "100%",
                layout: "hbox",
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodigitofield",
                        name: "idProduto",                                
                        readOnly: true,
                        considerGetValues: false,
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
                                fieldLabel: "Quantidade a Entregar",                                        
                                name: "quantidadeEntregar",                                        
                                readOnly: true,
                                considerGetValues: false,
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
                                fieldLabel: "Quantidade",
                                name: "quantidade",                                
                                allowBlank: false,
                                width: 250,
                                decimalPrecision: 3
                            },                                                        
                            {
                                xtype: "combo",
                                fieldLabel: "<b>Tipo Baixa</b>",
                                name: "tipoBaixa",                
                                width: 240,
                                store: Ext.create("Ext.data.ArrayStore" ,{
                                    fields: ["value", "text"],
                                    data: [
                                        [null, "Selecione"],
                                        ["1", "1 - Entrega"],                        
                                    ]
                                }),
                                queryMode: "local",
                                emptyText: "Selecione",
                                valueField: "value",
                                value: "1",
                                displayField: "text",
                                editable: false,
                                allowBlank: false,
                                enableKeyEvents: true
                            }
                        ]
                    }
                ]
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
        ];
        me.callParent(arguments);
        
        setFieldsFix(me);
    }
});