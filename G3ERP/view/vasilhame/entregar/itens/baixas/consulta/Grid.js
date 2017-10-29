Ext.define("App.view.vasilhame.entregar.itens.baixas.consulta.Grid", {
    extend: "App.util.ConsultasGrid",
    alias : "widget.entregarItemBaixaConGrid",

    requires: [
        "App.util.ConsultasGrid",
        "App.util.ConsultasTopBar",
        "App.util.mascaras.NumericField",
        "App.store.Ordenar"
    ],

    nomestore: "App.store.vasilhame.entregar.itens.baixas.Baixas",
    filterEncode: false,

    initComponent: function() {
        var me = this;
        
        if (!(me.params.ordens)) {
            me.params.ordens = new Array();
            if ( !(EMPRESAOCULTAR200) && !(EMPRESADESABILITAR200) ) {
                me.params.ordens.push({property: "idEmpresa", direction: CONSULTASORDENAR});
            }
            me.params.ordens.push(
                {property: "idFilial", direction: CONSULTASORDENAR},
                {property: "id", direction: CONSULTASORDENAR}
            );
        }
        var hideable = true;
        if (EMPRESAOCULTAR200) {
            hideable = false;
        }
                
        me.columns = { 
            defaults: {
                sortable: true,
                filterable: true
            },
            items: [
                {
                    xtype: "templatecolumn", 
                    text: "Empresa", 
                    dataIndex: "idEmpresa",
                    hidden: EMPRESAOCULTAR200,
                    hideable: hideable,
                    disabled: EMPRESADESABILITAR200,
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idEmpresa} - {empresa.nome}"
                },
                {
                    xtype: "templatecolumn", 
                    text: "Filial", 
                    dataIndex: "idFilial",
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idFilial} - {filial.descricao}"
                },
                {
                    text: "ID", 
                    dataIndex: "idCodigo",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Vasilhame", 
                    dataIndex: "vasilhameEntregarItem.idVasilhameEntregar",
                    hidden: true,
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Pessoa", 
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.idPessoa",
                    hidden: true,
                    width: 75,
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    renderer: function(value, metaData, record) { 
                        return value + "-" + record.data["vasilhameEntregarItem.vasilhameEntregar.pessoa.digito"];
                    } 
                },
                {
                    text: "Nome Pessoa", 
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.nome",
                    hidden: true,
                    flex: 1,
                    filter: {
                        xtype: "textfield"
                    }
                },
                {
                    text: "Contribuinte", 
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.tipoContribuinte",
                    hidden: true,
                    width: 150,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["1", "1 - Pessoa Física"],
                                ["2", "2 - Pessoa Jurídica"]           
                            ]
                        }),
                        queryMode: "local",
                        value: "",
                        valueField: "value",
                        displayField: "text",
                        editable: false
                    },
                    renderer: function(value, grid) { 
                        return renderComboboxValues({value: value, column: grid.column});
                    }
                },
                {
                    text: "CPF/CNPJ", 
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.cpfcnpj",
                    hidden: true,
                    width: 130,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numero"
                    },
                    renderer: function(value, metaData, record) {
                        if (record.data.vasilhameEntregarItem.vasilhameEntregar.tipoContribuinte) {
                            return mascaraIF(record.data.vasilhameEntregarItem.vasilhameEntregar.tipoContribuinte.toString(), value);
                        }
                    }                    
                },
                {
                    text: "Situação Vasilhame", 
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.situacao",
                    hidden: true,
                    width: 140,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["1", "1 - Aberto"],
                                ["2", "2 - Pendente"],
                                ["8", "8 - Inutilizado"],
                                ["9", "9 - Fechado"]                                
                            ]
                        }),
                        queryMode: "local",
                        value: "",
                        valueField: "value",
                        displayField: "text",
                        editable: false
                    },
                    renderer: function(value, grid) { 
                        return renderComboboxValues({value: value, column: grid.column});
                    }
                },
                {
                    xtype: "templatecolumn", 
                    text: "Usuário Vasilhame", 
                    hidden: true,
                    dataIndex: "vasilhameEntregarItem.vasilhameEntregar.idUsuario",
                    flex: 1,
                    filter: {
                        xtype: "textfield"
                    },
                    tpl: "{vasilhameEntregarItem.vasilhameEntregar.idUsuario} - {vasilhameEntregarItem.vasilhameEntregar.usuario.nome}"
                },
                {
                    text: "Vasilhame Item", 
                    dataIndex: "idVasilhameEntregarItem",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Produto", 
                    hidden: true,
                    dataIndex: "vasilhameEntregarItem.idProduto",
                    width: 100,
                    align: "center",                    
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },                    
                    xtype: "templatecolumn", 
                    tpl: "{vasilhameEntregarItem.idProduto}-{vasilhameEntregarItem.produto.digito}"
                },                                
                {                    
                    text: "Descrição", 
                    hidden: true,
                    dataIndex: "vasilhameEntregarItem.produto.descricao",
                    flex: 1,
                    filter: {
                        xtype: "textfield"                        
                    }                                
                },
                {
                    text: "Garrafeira", 
                    hidden: true,
                    dataIndex: "vasilhameEntregarItem.tipoGarrafeira",
                    width: 150,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["0", "0 - Não"],
                                ["1", "1 - Sim"]
                            ]
                        }),
                        queryMode: "local",
                        value: null,
                        valueField: "value",
                        displayField: "text",
                        editable: false
                    },
                    renderer: function(value, grid) { 
                        return renderComboboxValues({value: value, column: grid.column});
                    }
                },
                {
                    text: "Situação Item", 
                    dataIndex: "vasilhameEntregarItem.situacao",
                    hidden: true,
                    width: 150,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["1", "1 - Normal"],
                                ["2", "2 - Cancelado"]
                            ]
                        }),
                        queryMode: "local",
                        value: null,
                        valueField: "value",
                        displayField: "text",
                        editable: false
                    },
                    renderer: function(value, grid) { 
                        return renderComboboxValues({value: value, column: grid.column});
                    }
                },
                {
                    text: "Tipo Baixa", 
                    dataIndex: "tipoBaixa",
                    width: 150,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["1", "1 - Entrega"]                                
                            ]
                        }),
                        queryMode: "local",
                        value: null,
                        valueField: "value",
                        displayField: "text",
                        editable: false
                    },
                    renderer: function(value, grid) { 
                        return renderComboboxValues({value: value, column: grid.column});
                    }
                },                
                {
                    xtype: "numbercolumn",
                    text: "Quantidade a Entregar", 
                    dataIndex: "vasilhameEntregarItem.quantidade",                    
                    align: "right",
                    width: 130,
                    format: "0,0.000",
                    sortable: false,
                    filterable: false                       
                },
                {
                    xtype: "numbercolumn",
                    text: "Quantidade Baixada", 
                    dataIndex: "quantidadeBaixada",                    
                    align: "right",
                    width: 130,
                    format: "0,0.000",
                    sortable: false,
                    filterable: false   
                },
                {
                    xtype: "numbercolumn",
                    text: "Quantidade", 
                    dataIndex: "quantidade",                    
                    align: "right",
                    width: 115,
                    format: "0,0.000",
                    filter: {
                        xtype: "numericfield",
                        decimalPrecision: 3
                    }
                },
                {
                    xtype: "datecolumn",
                    text: "Data/Hora Gravação", 
                    dataIndex: "dataHoraGravacao",
                    width: 150,                    
                    align: "center",
                    format: "d/m/Y H:i:s",
                    hidden: true,
                    filter: {
                        xtype: "datefield",                        
                        format: "d/m/Y H:i:s",
                        submitFormat: "Y-m-d H:i:s"
                    }
                },
                {
                    xtype: "templatecolumn", 
                    text: "Usuário", 
                    dataIndex: "idUsuario",
                    flex: 1,
                    filter: {
                        xtype: "textfield"
                    },
                    tpl: "{idUsuario} - {usuario.nome}"
                }
            ]
        };
        me.callParent(arguments);
        
        setColumnsHidden(me);
    }
});