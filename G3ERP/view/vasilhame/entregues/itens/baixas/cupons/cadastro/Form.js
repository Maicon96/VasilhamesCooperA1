Ext.define("App.view.vasilhame.entregues.itens.baixas.cupons.cadastro.Form", {
    extend: "App.util.EdicoesForm", 
    alias : "widget.entregueItemBaixaCupomCadForm",
    
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
        
        var conProdutos = clone(formButtonConsultar);
        conProdutos.name = "conProdutos";
        conProdutos.tooltip = "Consulta de Produtos";
        conProdutos.disabled = true;
        
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
                        title: "Dados do Cupom Fiscal",
                        anchor: "100%",   
                        items: [
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
                                xtype: "numericfield",
                                fieldLabel: "Quantidade",                                        
                                name: "quantidade",                                        
                                readOnly: true,
                                considerGetValues: false,
                                width: 230,
                                decimalPrecision: 3
                            },
                            {
                                xtype: "numericfield",
                                fieldLabel: "Preço",
                                name: "preco",
                                readOnly: true,
                                considerGetValues: false,
                                width: 230,
                                decimalPrecision: 4
                            },
                            {
                                xtype: "numericfield",
                                fieldLabel: "Valor",
                                name: "valor",
                                readOnly: true,
                                considerGetValues: false,
                                width: 230,
                                decimalPrecision: 4
                            },
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
                                        name: "idEmpresaCupom",
                                        allowBlank: false,
                                        value: EMPRESACOD,
                                        readOnly: EMPRESADESABILITAR100,
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
                                fieldLabel: "<b>Filial</b>",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idFilialCupom",
                                        allowBlank: false,
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
                                fieldLabel: "<b>ECF</b>",
                                anchor: "100%",
                                layout: "hbox",
                                fieldDefaults: formFieldDefaults,
                                items: [
                                    {
                                        xtype: "combodefaultfield",
                                        name: "idEcfCupom",
                                        allowBlank: false,
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
                            },
                            {
                                xtype: "textfield",
                                vtype: "numero",
                                fieldLabel: "<b>Cupom</b>",
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