Ext.define("App.view.vasilhame.entregues.itens.baixas.notas.relatorio.Window", {
    extend: "App.util.Windows",
    alias: "widget.entregueItemBaixaNotaRelWindow",
    
    requires: [
        "App.util.Windows"
    ],

    width: 780,
    height: 450,
    maximized: true,

    initComponent: function() {
        var me = this;
        me.items = [
            {
                xtype: "entregueItemBaixaNotaRelForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});
    