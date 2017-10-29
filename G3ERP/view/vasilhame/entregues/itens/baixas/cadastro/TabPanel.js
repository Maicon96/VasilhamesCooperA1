Ext.define("App.view.vasilhame.entregues.itens.baixas.cadastro.TabPanel" ,{
    extend: "Ext.tab.Panel",
    alias: "widget.entregueItemBaixaCadTabPanel",
    
    plain: true,
    activeTab: 0,
    defaults: {
        layout: "anchor"
    },
    frame: true,
    
    items: [
        {
            xtype: "entregueItemBaixaCadNota"
        },
        {
            xtype: "entregueItemBaixaCadCupom"
        }
    ]
});
