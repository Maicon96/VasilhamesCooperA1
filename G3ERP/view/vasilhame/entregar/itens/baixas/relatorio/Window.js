Ext.define("App.view.vasilhame.entregar.itens.baixas.relatorio.Window", {
    extend: "App.util.Windows",
    alias: "widget.entregarItemBaixaRelWindow",
    
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
                xtype: "entregarItemBaixaRelForm",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }
});
    