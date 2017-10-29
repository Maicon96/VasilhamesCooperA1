Ext.define("App.view.vasilhame.entregues.itens.baixas.notas.consulta.Grid", {
    extend: "App.util.ConsultasGrid",
    alias : "widget.entregueItemBaixaNotaConGrid",

    requires: [
        "App.util.ConsultasGrid",
        "App.util.ConsultasTopBar",
        "App.util.mascaras.NumericField",
        "App.store.Ordenar"
    ],

    nomestore: "App.store.vasilhame.entregues.itens.baixas.notas.Notas",
    filterEncode: false,

    initComponent: function() {
        var me = this;
        
        if (!(me.params.ordens)) {
            me.params.ordens = new Array();
            if ( !(EMPRESAOCULTAR200) && !(EMPRESADESABILITAR200) ) {
                me.params.ordens.push({property: "idEmpresaBaixa", direction: CONSULTASORDENAR});
            }
            me.params.ordens.push(
                {property: "idFilialBaixa", direction: CONSULTASORDENAR},
                {property: "idVasilhameEntregueItemBaixa", direction: CONSULTASORDENAR}
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
                    text: "Empresa Baixa", 
                    dataIndex: "idEmpresaBaixa",
                    hidden: EMPRESAOCULTAR200,
                    hideable: hideable,
                    disabled: EMPRESADESABILITAR200,
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idEmpresaBaixa} - {empresaBaixa.nome}"
                },
                {
                    xtype: "templatecolumn", 
                    text: "Filial Baixa", 
                    dataIndex: "idFilialBaixa",
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idFilialBaixa} - {filialBaixa.descricao}"
                },
                {
                    text: "Vasilhame", 
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idVasilhameEntregue",
                    width: 100,
                    hidden: true,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Pessoa", 
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idPessoa",
                    align: "center",
                    hidden: true,
                    width: 75,
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    renderer: function(value, metaData, record) { 
                        return value + "-" + record.data["vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.pessoa.digito"];
                    } 
                },
                {
                    text: "Nome Pessoa", 
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.nome",
                    hidden: true,
                    flex: 1,
                    filter: {
                        xtype: "textfield"
                    }
                },
                {
                    text: "Contribuinte", 
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.tipoContribuinte",
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
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.cpfcnpj",
                    hidden: true,
                    width: 130,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numero"
                    },
                    renderer: function(value, metaData, record) {
                        if (record.data.vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.tipoContribuinte) {
                            return mascaraIF(record.data.vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.tipoContribuinte.toString(), value);
                        }
                    }                    
                },
                {
                    text: "Situação Vasilhame", 
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.situacao",
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
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idUsuario",
                    flex: 1,
                    filter: {
                        xtype: "textfield"
                    },
                    tpl: "{vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idUsuario} - {vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.usuario.nome}"
                },
                {
                    text: "Vasilhame Item", 
                    dataIndex: "vasilhameEntregueItemBaixa.idVasilhameEntregueItem",
                    width: 100,
                    hidden: true,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Produto", 
                    hidden: true,
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto",
                    width: 100,
                    align: "center",                    
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },                    
                    xtype: "templatecolumn", 
                    tpl: "{vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto}-{vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.digito}"
                },                                
                {                    
                    text: "Descrição", 
                    hidden: true,
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.descricao",
                    flex: 1,
                    filter: {
                        xtype: "textfield"                        
                    }                                
                },
                {
                    text: "Garrafeira", 
                    hidden: true,
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.tipoGarrafeira",
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
                    dataIndex: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.situacao",
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
                    text: "ID Baixa", 
                    dataIndex: "idVasilhameEntregueItemBaixa",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Tipo Baixa", 
                    dataIndex: "vasilhameEntregueItemBaixa.tipoBaixa",
                    hidden: true,
                    width: 150,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["1", "1 - Venda"],
                                ["2", "2 - Devolução"]
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
                    xtype: "templatecolumn", 
                    text: "Empresa Nota Fiscal", 
                    dataIndex: "idEmpresaNota",
                    hidden: EMPRESAOCULTAR200,
                    hideable: hideable,
                    disabled: EMPRESADESABILITAR200,
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idEmpresaNota} - {empresaNota.nome}"
                },
                {
                    text: "Modelo Nota Fiscal", 
                    dataIndex: "modelo",
                    width: 255,
                    filter: {
                        xtype: "combo",
                        store: Ext.create("Ext.data.ArrayStore" ,{
                            fields: ["value", "text"],
                            data: [
                                [null, "Todos"],
                                ["01", "01 - Nota Fiscal"],
                                ["1B", "1B - Nota Fiscal Avulsa"],
                                ["55", "55 - Nota Fiscal Eletrônica"],
                                ["65", "65 - Nota Fiscal de Consumidor Eletrônica"]
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
                    xtype: "templatecolumn", 
                    text: "Filial Nota Fiscal", 
                    dataIndex: "idFilialNota",
                    flex: 1,
                    align: "left",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros"
                    },
                    tpl: "{idFilialNota} - {filialNota.descricao}"
                },
                {
                    text: "Série", 
                    dataIndex: "serie",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    text: "Número", 
                    dataIndex: "numero",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                },
                {
                    xtype: "datecolumn",
                    text: "Data Emissão", 
                    dataIndex: "dataEmissao",
                    width: 110,                    
                    align: "center",
                    format: "d/m/Y",
                    filter: {
                        xtype: "datefield",                        
                        format: "d/m/Y",
                        submitFormat: "Y-m-d"
                    }
                },
                {
                    text: "Sequência", 
                    dataIndex: "sequencia",
                    width: 100,
                    align: "center",
                    filter: {
                        xtype: "textfield",
                        vtype: "numeros" 
                    }
                }
            ]
        };
        me.callParent(arguments);
        
        setColumnsHidden(me);
    }
});