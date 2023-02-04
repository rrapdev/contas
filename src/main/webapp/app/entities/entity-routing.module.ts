import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'conta-pagamento',
        data: { pageTitle: 'contasApp.contaPagamento.home.title' },
        loadChildren: () => import('./conta-pagamento/conta-pagamento.module').then(m => m.ContaPagamentoModule),
      },
      {
        path: 'pagador',
        data: { pageTitle: 'contasApp.pagador.home.title' },
        loadChildren: () => import('./pagador/pagador.module').then(m => m.PagadorModule),
      },
      {
        path: 'categoria-pagamento',
        data: { pageTitle: 'contasApp.categoriaPagamento.home.title' },
        loadChildren: () => import('./categoria-pagamento/categoria-pagamento.module').then(m => m.CategoriaPagamentoModule),
      },
      {
        path: 'conta-recebimento',
        data: { pageTitle: 'contasApp.contaRecebimento.home.title' },
        loadChildren: () => import('./conta-recebimento/conta-recebimento.module').then(m => m.ContaRecebimentoModule),
      },
      {
        path: 'beneficiario',
        data: { pageTitle: 'contasApp.beneficiario.home.title' },
        loadChildren: () => import('./beneficiario/beneficiario.module').then(m => m.BeneficiarioModule),
      },
      {
        path: 'categoria-recebimento',
        data: { pageTitle: 'contasApp.categoriaRecebimento.home.title' },
        loadChildren: () => import('./categoria-recebimento/categoria-recebimento.module').then(m => m.CategoriaRecebimentoModule),
      },
      {
        path: 'caixa',
        data: { pageTitle: 'contasApp.caixa.home.title' },
        loadChildren: () => import('./caixa/caixa.module').then(m => m.CaixaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
