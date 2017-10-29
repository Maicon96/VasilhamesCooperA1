Ext.define("App.store.vasilhame.entregues.itens.baixas.notas.Notas", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregues.itens.baixas.notas.Notas",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/save",
            read: BACKEND + "/vasilhame/entregues/itens/baixas/notas/consulta/load",
            update: BACKEND + "/vasilhame/entregues/itens/baixas/notas/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregues/itens/baixas/notas/consulta/delete"
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
