package school.sptech.service;

import school.sptech.dao.EmpresaDAO;
import school.sptech.model.Empresa;

public class EmpresaService {
    private final EmpresaDAO empresaDAO;

    public EmpresaService(EmpresaDAO empresaDao) {
        this.empresaDAO = empresaDao;
    }

    public Empresa buscarEmpresaEValidarCredenciaisJira(Integer idEmpresa) {
        Empresa empresa = empresaDAO.getById(idEmpresa);
        if (empresa == null) {
            System.out.println("[ AVISO ] Nenhuma empresa encontrada para o ID: " + idEmpresa);
            return null;
        }

        if (empresa.getJiraEmail() == null || empresa.getJiraApiKey() == null
                || empresa.getJiraBaseUrl() == null || empresa.getJiraProjectKey() == null) {
            System.out.printf("[ AVISO ] A empresa %s não configurou todas as credenciais do Jira\n", empresa.getRazaoSocial());
            return null;
        }

        return empresa;
    }
}
