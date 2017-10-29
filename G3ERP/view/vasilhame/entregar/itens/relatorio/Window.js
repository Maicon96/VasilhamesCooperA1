Ext.define("App.view.vasilhame.entregar.itens.relatorio.Window", {
    extend: "App.util.Windows",
    alias: "widget.entregarItemRelWindow",
    
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
                xtype: "entregarItemRelForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});
    