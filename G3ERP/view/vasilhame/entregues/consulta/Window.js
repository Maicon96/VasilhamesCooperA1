Ext.define("App.view.vasilhame.entregues.consulta.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregueConWindow",
    
    requires: [
        "App.util.Windows"
    ],
    
    width: 800,
    height: HEIGHTWINDOW200,
    maximized: true,
    
    initComponent: function() {
        var me = this;
        me.items = [
            {
                xtype: "entregueConGrid",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }    
});
    