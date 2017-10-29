Ext.define("App.view.vasilhame.entregues.itens.baixas.cadastro.ButtonGroup", {
    extend: "Ext.panel.Panel",
    alias: "widget.entregueItemBaixaCadButtonGroup",

    initComponent: function() {
        var me = this;
        
        me.dockedItems = [{
            xtype: "toolbar",
            dock: "top",
            enableOverflow: true,
            items: [
                {
                    text: "Notas",
                    name: "nota"
                },
                {
                    text: "Cupons",
                    name: "cupom"
                }
            ]
        }];
        
        me.callParent(arguments);
    }
});