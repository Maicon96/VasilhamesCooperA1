Ext.define("App.store.vasilhame.entregar.itens.baixas.Baixas", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregar.itens.baixas.Baixas",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/save",
            read: BACKEND + "/vasilhame/entregar/itens/baixas/consulta/load",
            update: BACKEND + "/vasilhame/entregar/itens/baixas/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregar/itens/baixas/consulta/delete"
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
