import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContaPagamentoDetailComponent } from './conta-pagamento-detail.component';

describe('ContaPagamento Management Detail Component', () => {
  let comp: ContaPagamentoDetailComponent;
  let fixture: ComponentFixture<ContaPagamentoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContaPagamentoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contaPagamento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContaPagamentoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContaPagamentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contaPagamento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contaPagamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
