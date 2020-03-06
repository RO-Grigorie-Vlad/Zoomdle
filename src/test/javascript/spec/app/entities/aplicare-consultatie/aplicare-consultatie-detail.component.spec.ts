import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LicentaTestModule } from '../../../test.module';
import { AplicareConsultatieDetailComponent } from 'app/entities/aplicare-consultatie/aplicare-consultatie-detail.component';
import { AplicareConsultatie } from 'app/shared/model/aplicare-consultatie.model';

describe('Component Tests', () => {
  describe('AplicareConsultatie Management Detail Component', () => {
    let comp: AplicareConsultatieDetailComponent;
    let fixture: ComponentFixture<AplicareConsultatieDetailComponent>;
    const route = ({ data: of({ aplicareConsultatie: new AplicareConsultatie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LicentaTestModule],
        declarations: [AplicareConsultatieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AplicareConsultatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AplicareConsultatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aplicareConsultatie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aplicareConsultatie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
