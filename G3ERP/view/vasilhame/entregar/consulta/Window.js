Ext.define("App.view.vasilhame.entregar.consulta.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregarConWindow",
    
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
                xtype: "entregarConGrid",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }    
});
    