Ext.define("App.view.vasilhame.entregar.itens.cadastro.TabPanel" ,{
    extend: "Ext.tab.Panel",
    alias: "widget.entregarItemCadTabPanel",
    
    plain: true,
    activeTab: 0,
    defaults: {
        layout: "anchor"
    },
    frame: true,
    
    items: [
        {
            xtype: "entregarItemCadBaixa"
        }
    ]
});
