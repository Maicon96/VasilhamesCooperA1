Ext.define("App.view.vasilhame.entregar.cadastro.Form", {
    extend: "App.util.EdicoesForm", 
    alias : "widget.entregarCadForm",
    
    requires: [
        "App.util.EdicoesForm",
        "App.util.mascaras.NumericField",
        "App.util.mascaras.CPFsCNPJsField",
        "App.util.mascaras.ComboDigitoField",
        "App.util.mascaras.ComboDefaultField"
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
             
        var conPessoas = clone(formButtonConsultar);
        conPessoas.name = "conPessoas";
        conPessoas.tooltip = "Consulta de Pessoas";
        
        var conUsuarios = clone(formButtonConsultar);
        conUsuarios.name = "conUsuarios";
        conUsuarios.tooltip = "Consulta Usuários";
        conUsuarios.disabled = true;
                
        var itemsFormCampos = [
            {
                xtype: "container",
                name: "containerCampos",
                flex: 0.8,
                autoScroll: true,
                layout: "anchor",
                items: [
                    {
                        xtype: "fieldcontainer",
                        fieldLabel: "<b>Pessoa</b>",
                        anchor: "95%",
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults,
                        items: [
                            {
                                xtype: "combodigitofield",
                                name: "idPessoa",
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
                                        comparison: "eq",
                                        valueComparatorExternal: false
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
                                    },{
                                        field: "contribuinte_gepess",
                                        fieldSetValue: "tipoContribuinte"
                                    },{
                                        field: "cpfcnpj_gepess",
                                        fieldSetValue: "cpfcnpj"
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
                                allowBlank: false,
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
                        allowBlank: false,
                        editable: false 
                    },   
                    {
                        xtype: "cpfscnpjsfield",
                        fieldLabel: "CPF/CNPJ",
                        width: 280,
                        name: "cpfcnpj",
                        layout: "hbox",
                        fieldDefaults: formFieldDefaults         
                    },
                    {
                        xtype: "textareafield",
                        fieldLabel: "Observação",
                        enableKeyEvents: true,
                        name: "observacao",
                        maxLength: 300,
                        anchor: "100%"
                    },
                    {
                        xtype: "combo",
                        fieldLabel: "<b>Situação</b>",
                        name: "situacao",                
                        readOnly: true,
                        width: 240,
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [                               
                                ["1", "1 - Aberto"],
                                ["2", "2 - Pendente"],
                                ["8", "8 - Inutilizado"],
                                ["9", "9 - Fechado"]
                            ]
                        }),
                        queryMode: "local",
                        value: "1",
                        valueField: "value",
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
                        anchor: "98%",
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
                xtype: "entregarCadTabPanel",
                flex: 1,
                title: TABPANELTITLE,
                collapsible: true,
                collapseDirection: "bottom"
            });
        }
        if (me.modoCompl == "botoes") {                       
            itemsFormCampos.push({
                xtype: "entregarCadButtonGroup",
                title: TABPANELTITLE
            });
        }
        
       var itemsForm = [
            {
                xtype: "fieldcontainer",
                fieldLabel: "<b>Empresa</b>",
                anchor: "98%",
                layout: "hbox",
                hidden: EMPRESAOCULTAR300,
                fieldDefaults: formFieldDefaults,
                items: [
                    {
                        xtype: "combodefaultfield",
                        name: "idEmpresa",
                        allowBlank: false,
                        value: EMPRESACOD,
                        readOnly: EMPRESADESABILITAR300,
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
                anchor: "98%",
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
        
        var toolbarTop = me.getDockedItems("toolbar[dock='top']");
        
        var btn = {
            xtype: "button",
            name: "btnFinalizar",
            tooltip:"Finalizar Vasilhames a Entregar",
            iconCls:"finalizarPedido"     
        };  
        toolbarTop[0].insert(4, "-");
        toolbarTop[0].insert(5, btn);
        
        btn = {
            xtype: "button",
            name: "btnAbrir",
            tooltip:"Abrir Vasilhames a Entregar",
            iconCls:"abrirPedido"     
        };  
        toolbarTop[0].insert(6, btn);
        
        btn = {
            xtype: "button",
            name: "btnInutilizar",
            tooltip:"Inutilizar Vasilhames a Entregar",
            iconCls:"inutilizarPedido"     
        };  
        toolbarTop[0].insert(7, btn);
        toolbarTop[0].insert(8, "-");
        
        btn = {
            xtype: "button",
            name: "btnPDF",
            tooltip:"Gerar Impressão",
            iconCls:"pdf"     
        };  
        toolbarTop[0].insert(9, btn);
    }
});