Ext.define("App.view.vasilhame.entregar.cadastro.TabPanel" ,{
    extend: "Ext.tab.Panel",
    alias: "widget.entregarCadTabPanel",
    
    plain: true,
    activeTab: 0,
    defaults: {
        layout: "anchor"
    },
    frame: true,
    
    items: [
        {
            xtype: "entregarCadItem"
        }
    ]
});
