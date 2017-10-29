Ext.define("App.store.vasilhame.entregues.itens.baixas.Baixas", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregues.itens.baixas.Baixas",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregues/itens/baixas/cadastro/save",
            read: BACKEND + "/vasilhame/entregues/itens/baixas/consulta/load",
            update: BACKEND + "/vasilhame/entregues/itens/baixas/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregues/itens/baixas/consulta/delete"
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
