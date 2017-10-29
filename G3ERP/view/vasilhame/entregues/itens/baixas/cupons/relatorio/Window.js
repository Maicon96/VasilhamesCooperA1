Ext.define("App.view.vasilhame.entregues.itens.baixas.cupons.relatorio.Window", {
    extend: "App.util.Windows",
    alias: "widget.entregueItemBaixaCupomRelWindow",
    
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
                xtype: "entregueItemBaixaCupomRelForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});
    