Ext.define("App.view.vasilhame.entregues.itens.cadastro.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregueItemCadWindow",
    
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
                xtype: "entregueItemCadForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});    