Ext.define("App.view.vasilhame.entregues.itens.cadastro.TabPanel" ,{
    extend: "Ext.tab.Panel",
    alias: "widget.entregueItemCadTabPanel",
    
    plain: true,
    activeTab: 0,
    defaults: {
        layout: "anchor"
    },
    frame: true,
    
    items: [
        {
            xtype: "entregueItemCadBaixa"
        }
    ]
});
