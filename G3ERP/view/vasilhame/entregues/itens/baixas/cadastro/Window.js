Ext.define("App.view.vasilhame.entregues.itens.baixas.cadastro.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregueItemBaixaCadWindow",
    
    requires: [
        "App.util.Windows"
    ],
    
    width: 650,
    height: 235,
    maximized: true,
    
    initComponent: function() {
        var me = this;
        me.items = [
            {
                xtype: "entregueItemBaixaCadForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});    