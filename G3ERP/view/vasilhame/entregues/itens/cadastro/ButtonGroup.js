Ext.define("App.view.vasilhame.entregues.itens.cadastro.ButtonGroup", {
    extend: "Ext.panel.Panel",
    alias: "widget.entregueItemCadButtonGroup",

    initComponent: function() {
        var me = this;
        
        me.dockedItems = [{
            xtype: "toolbar",
            dock: "top",
            enableOverflow: true,
            items: [
                {
                    text: "Baixas",
                    name: "baixa"
                }
            ]
        }];
        
        me.callParent(arguments);
    }
});