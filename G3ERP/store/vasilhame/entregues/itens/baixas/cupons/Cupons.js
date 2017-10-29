Ext.define("App.store.vasilhame.entregues.itens.baixas.cupons.Cupons", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregues.itens.baixas.cupons.Cupons",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/save",
            read: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/consulta/load",
            update: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregues/itens/baixas/cupons/consulta/delete"
        },
        reader: {
            type: "json", 
            rootProperty: "registros"
        },
        writer: {
            type: "json", 
            rootProperty: "registros"              
        }
    }
});
