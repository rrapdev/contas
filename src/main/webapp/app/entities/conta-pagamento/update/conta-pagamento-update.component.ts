import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ContaPagamentoFormService, ContaPagamentoFormGroup } from './conta-pagamento-form.service';
import { IContaPagamento } from '../conta-pagamento.model';
import { ContaPagamentoService } from '../service/conta-pagamento.service';
import { IBeneficiario } from 'app/entities/beneficiario/beneficiario.model';
import { BeneficiarioService } from 'app/entities/beneficiario/service/beneficiario.service';
import { ICategoriaPagamento } from 'app/entities/categoria-pagamento/categoria-pagamento.model';
import { CategoriaPagamentoService } from 'app/entities/categoria-pagamento/service/categoria-pagamento.service';
import { ICaixa } from 'app/entities/caixa/caixa.model';
import { CaixaService } from 'app/entities/caixa/service/caixa.service';
import { Periodicidade } from 'app/entities/enumerations/periodicidade.model';

@Component({
  selector: 'jhi-conta-pagamento-update',
  templateUrl: './conta-pagamento-update.component.html',
})
export class ContaPagamentoUpdateComponent implements OnInit {
  isSaving = false;
  contaPagamento: IContaPagamento | null = null;
  periodicidadeValues = Object.keys(Periodicidade);

  beneficiariosSharedCollection: IBeneficiario[] = [];
  categoriaPagamentosSharedCollection: ICategoriaPagamento[] = [];
  caixasSharedCollection: ICaixa[] = [];

  editForm: ContaPagamentoFormGroup = this.contaPagamentoFormService.createContaPagamentoFormGroup();

  constructor(
    protected contaPagamentoService: ContaPagamentoService,
    protected contaPagamentoFormService: ContaPagamentoFormService,
    protected beneficiarioService: BeneficiarioService,
    protected categoriaPagamentoService: CategoriaPagamentoService,
    protected caixaService: CaixaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBeneficiario = (o1: IBeneficiario | null, o2: IBeneficiario | null): boolean =>
    this.beneficiarioService.compareBeneficiario(o1, o2);

  compareCategoriaPagamento = (o1: ICategoriaPagamento | null, o2: ICategoriaPagamento | null): boolean =>
    this.categoriaPagamentoService.compareCategoriaPagamento(o1, o2);

  compareCaixa = (o1: ICaixa | null, o2: ICaixa | null): boolean => this.caixaService.compareCaixa(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaPagamento }) => {
      this.contaPagamento = contaPagamento;
      if (contaPagamento) {
        this.updateForm(contaPagamento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contaPagamento = this.contaPagamentoFormService.getContaPagamento(this.editForm);
    if (contaPagamento.id !== null) {
      this.subscribeToSaveResponse(this.contaPagamentoService.update(contaPagamento));
    } else {
      this.subscribeToSaveResponse(this.contaPagamentoService.create(contaPagamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContaPagamento>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contaPagamento: IContaPagamento): void {
    this.contaPagamento = contaPagamento;
    this.contaPagamentoFormService.resetForm(this.editForm, contaPagamento);

    this.beneficiariosSharedCollection = this.beneficiarioService.addBeneficiarioToCollectionIfMissing<IBeneficiario>(
      this.beneficiariosSharedCollection,
      contaPagamento.beneficiario
    );
    this.categoriaPagamentosSharedCollection =
      this.categoriaPagamentoService.addCategoriaPagamentoToCollectionIfMissing<ICategoriaPagamento>(
        this.categoriaPagamentosSharedCollection,
        contaPagamento.categoriaPagamento
      );
    this.caixasSharedCollection = this.caixaService.addCaixaToCollectionIfMissing<ICaixa>(
      this.caixasSharedCollection,
      contaPagamento.caixa
    );
  }

  protected loadRelationshipsOptions(): void {
    this.beneficiarioService
      .query()
      .pipe(map((res: HttpResponse<IBeneficiario[]>) => res.body ?? []))
      .pipe(
        map((beneficiarios: IBeneficiario[]) =>
          this.beneficiarioService.addBeneficiarioToCollectionIfMissing<IBeneficiario>(beneficiarios, this.contaPagamento?.beneficiario)
        )
      )
      .subscribe((beneficiarios: IBeneficiario[]) => (this.beneficiariosSharedCollection = beneficiarios));

    this.categoriaPagamentoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaPagamento[]>) => res.body ?? []))
      .pipe(
        map((categoriaPagamentos: ICategoriaPagamento[]) =>
          this.categoriaPagamentoService.addCategoriaPagamentoToCollectionIfMissing<ICategoriaPagamento>(
            categoriaPagamentos,
            this.contaPagamento?.categoriaPagamento
          )
        )
      )
      .subscribe((categoriaPagamentos: ICategoriaPagamento[]) => (this.categoriaPagamentosSharedCollection = categoriaPagamentos));

    this.caixaService
      .query()
      .pipe(map((res: HttpResponse<ICaixa[]>) => res.body ?? []))
      .pipe(map((caixas: ICaixa[]) => this.caixaService.addCaixaToCollectionIfMissing<ICaixa>(caixas, this.contaPagamento?.caixa)))
      .subscribe((caixas: ICaixa[]) => (this.caixasSharedCollection = caixas));
  }
}
