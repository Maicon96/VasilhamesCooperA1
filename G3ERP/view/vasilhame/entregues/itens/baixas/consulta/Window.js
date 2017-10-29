Ext.define("App.view.vasilhame.entregues.itens.baixas.consulta.Window" ,{
    extend: "App.util.Windows",
    alias: "widget.entregueItemBaixaConWindow",
    
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
                xtype: "entregueItemBaixaConGrid",
                anchor: "100% 100%",
                params: me.params
            }
        ];
        me.callParent(arguments);
    }    
});
    