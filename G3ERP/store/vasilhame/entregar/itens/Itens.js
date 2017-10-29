Ext.define("App.store.vasilhame.entregar.itens.Itens", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregar.itens.Itens",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregar/itens/cadastro/save",
            read: BACKEND + "/vasilhame/entregar/itens/consulta/load",
            update: BACKEND + "/vasilhame/entregar/itens/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregar/itens/consulta/delete"
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
