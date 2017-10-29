Ext.define("App.view.vasilhame.entregues.cadastro.TabPanel" ,{
    extend: "Ext.tab.Panel",
    alias: "widget.entregueCadTabPanel",
    
    plain: true,
    activeTab: 0,
    defaults: {
        layout: "anchor"
    },
    frame: true,
    
    items: [
        {
            xtype: "entregueCadItem"
        }
    ]
});
