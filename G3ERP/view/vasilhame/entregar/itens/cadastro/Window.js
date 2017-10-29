Ext.define("App.view.vasilhame.entregar.itens.cadastro.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregarItemCadWindow",
    
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
                xtype: "entregarItemCadForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});    