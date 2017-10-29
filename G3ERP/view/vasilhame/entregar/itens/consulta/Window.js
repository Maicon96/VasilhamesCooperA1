Ext.define("App.view.vasilhame.entregar.itens.consulta.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregarItemConWindow",
    
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
                xtype: "entregarItemConGrid",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }    
});
    