Ext.define("App.view.vasilhame.entregues.itens.baixas.notas.cadastro.Form", {
    extend: "App.util.EdicoesForm", 
    alias : "widget.entregueItemBaixaNotaCadForm",
    
    requires: [
        "App.util.EdicoesForm",
        "App.util.mascaras.ComboDefaultField"
    ],
    
    initComponent: function() {
        var me = this;
        Ext.apply(me.fieldDefaults, {
            labelWidth: 85,
            considerGetValues: true
        });
        
        var conEmpresasBaixas = clone(formButtonConsultar);
        conEmpresasBaixas.name = "conEmpresasBaixas";
        conEmpresasBaixas.tooltip = "Consulta de Empresas";
        conEmpresasBaixas.disabled = EMPRESADESABILITAR100;
        
        var conFiliaisBaixas = clone(formButtonConsultar);
        conFiliaisBaixas.name = "conFiliaisBaixas";
        conFiliaisBaixas.tooltip = "Consulta de Filiais";
             
        var conVasilhamesItensBaixas = clone(formButtonConsultar);
        conVasilhamesItensBaixas.name = "conVasilhamesItensBaixas";
        conVasilhamesItensBaixas.tooltip = "Consulta de Vasilhames dos Itens das Baixas";
        
        var conEmpresasNotas = clone(formButtonConsultar);
        conEmpresasNotas.name = "conEmpresasNotas";
        conEmpresasNotas.tooltip = "Consulta de Empresas";
        conEmpresasNotas.disabled = EMPRESADESABILITAR100;
        
        var conFiliaisNotas = clone(formButtonConsultar);
        conFiliaisNotas.name = "conFiliaisNotas";
        conFiliaisNotas.tooltip = "Consulta de Filiais";
                
        me.items = [
            {
                xtype: "fieldcontainer",
                anchor: "100%",     
                items:[
                    {
                        xtype: "fieldset",
                        defaultType: "textfield",
                        title: "Dados da Baixa",
                        anchor: "100%",   
                        items: [
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
                                        name: "idEmpresaBaixa",
                                        allowBlank: false,
                                        value: EMPRESACOD,
                                        readOnly: EMPRESADESABILITAR100,
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
                                fieldLabel: "<b>Filial</b>",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idFilialBaixa",
                                        allowBlank: false,
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
                                fieldLabel: "<b>ID Baixa</b>",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idVasilhameEntregueItemBaixa",
                                        allowBlank: false,
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
                        title: "Dados da Nota Fiscal",
                        anchor: "100%",   
                        items: [
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
                                        name: "idEmpresaNota",
                                        allowBlank: false,
                                        value: EMPRESACOD,
                                        readOnly: EMPRESADESABILITAR100,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.geempr200_.Empresas",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idEmpresaNota",
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
                                                fieldSetValue: "idEmpresaNota"
                                            },{
                                                field: "nome",
                                                fieldSetValue: "nomeEmpresaNota"
                                            }]
                                        }),
                                        valueField: "idEmpresaNota",
                                        displayField: "idEmpresaNota",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{id} - {nome}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conEmpresasNotas,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "nomeEmpresaNota",
                                        value: EMPRESANOM,
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "combo",
                                fieldLabel: "<b>Modelo</b>",
                                name: "modelo",                
                                width: 325,
                                store: Ext.create("Ext.data.ArrayStore" ,{
                                    fields: ["value", "text"],
                                    data: [
                                        [null, "Selecione"],
                                        ["01", "01 - Nota Fiscal"],
                                        ["1B", "1B - Nota Fiscal Avulsa"],
                                        ["55", "55 - Nota Fiscal Eletrônica"],
                                        ["65", "65 - Nota Fiscal de Consumidor Eletrônica"]
                                    ]
                                }),
                                queryMode: "local",
                                emptyText: "Selecione",
                                valueField: "value",
                                displayField: "text",
                                editable: false,
                                allowBlank: false
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
                                        name: "idFilialNota",
                                        allowBlank: false,
                                        store: createStoreCombobox({
                                            store: "App.store.geral.gefili200_.Filiais",
                                            form: this,
                                            filterEncode: false,                            
                                            fieldAutoComplete: "idFilialNota",
                                            sorters: [{property: "descricao", direction: CONSULTASORDENAR}],
                                            fieldsFilterFix: [{
                                                field: "idEmpresa",
                                                value: 0,
                                                getValueFieldForm: "idEmpresaNota",
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
                                                fieldSetValue: "idFilialNota"
                                            },{
                                                field: "descricao",
                                                fieldSetValue: "descricaoFilialNota"
                                            }]
                                        }),
                                        valueField: "idFilialNota",
                                        displayField: "idFilialNota",
                                        tpl: Ext.create('Ext.XTemplate',
                                            '<ul class="x-list-plain"><tpl for=".">',
                                                '<li role="option" class="x-boundlist-item">{codigo} - {descricao}</li>',
                                            '</tpl></ul>'
                                        ),
                                        width: 85
                                    },
                                        conFiliaisNotas,
                                    {
                                        xtype: "splitter"
                                    },                    
                                    {
                                        xtype: "textfield",
                                        name: "descricaoFilialNota",
                                        considerGetValues: false,
                                        readOnly: true,
                                        flex: 1
                                    }
                                ]            
                            },
                            {
                                xtype: "textfield",
                                vtype: "numero",
                                fieldLabel: "<b>Série</b>",
                                name: "serie",
                                allowBlank: false,
                                width: 175
                            },
                            {
                                xtype: "textfield",
                                vtype: "numero",
                                fieldLabel: "<b>Número</b>",
                                name: "numero",
                                allowBlank: false,
                                width: 175
                            },       
                            {
                                xtype: "datefield",
                                fieldLabel: "<b>Data Emissão</b>",
                                name: "dataEmissao",
                                format: "d/m/Y",
                                submitFormat: "Y-m-d",
                                allowBlank: false,
                                width: 192
                            },
                            {
                                xtype: "textfield",
                                vtype: "numero",
                                fieldLabel: "<b>Sequência</b>",
                                name: "sequencia",
                                allowBlank: false,
                                enableKeyEvents: true,
                                width: 175
                            }
                        ]
                    }
                ]
            },
            {
                html: "<hr />",
                frame: true,
                style: {
                    border: 0,
                    height: 5
                }            
            }
        ];
        me.callParent(arguments);
        
        setFieldsFix(me);
    }
});