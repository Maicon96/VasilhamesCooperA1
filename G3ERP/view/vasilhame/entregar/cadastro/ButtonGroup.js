Ext.define("App.view.vasilhame.entregar.cadastro.ButtonGroup", {
    extend: "Ext.panel.Panel",
    alias: "widget.entregarCadButtonGroup",

    initComponent: function() {
        var me = this;
        
        me.dockedItems = [{
            xtype: "toolbar",
            dock: "top",
            enableOverflow: true,
            items: [
                {
                    text: "Itens",
                    name: "item"
                }
            ]
        }];
        
        me.callParent(arguments);
    }
});
