package school.sptech.service;

import school.sptech.dao.EmpresaDAO;
import school.sptech.model.Empresa;

public class EmpresaService {
    private final EmpresaDAO empresaDAO;

    public EmpresaService(EmpresaDAO empresaDao) {
        this.empresaDAO = empresaDao;
    }

    public Boolean credenciaisJiraValidas(Empresa empresa) {
        if(empresa == null) return false;

        if(
                empresa.getJiraEmail() == null ||
                empresa.getJiraApiKey() == null ||
                empresa.getJiraBaseUrl() == null ||
                empresa.getJiraProjectKey() == null
        ) {
            System.out.printf("[ AVISO ] A empresa %s não configurou todas as credenciais do Jira\n", empresa.getRazaoSocial());
            return false;
        };

        return true;
    }
}
