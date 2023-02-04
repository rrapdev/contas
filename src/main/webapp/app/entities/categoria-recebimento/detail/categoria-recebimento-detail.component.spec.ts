import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaRecebimentoDetailComponent } from './categoria-recebimento-detail.component';

describe('CategoriaRecebimento Management Detail Component', () => {
  let comp: CategoriaRecebimentoDetailComponent;
  let fixture: ComponentFixture<CategoriaRecebimentoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoriaRecebimentoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ categoriaRecebimento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CategoriaRecebimentoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoriaRecebimentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load categoriaRecebimento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.categoriaRecebimento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
