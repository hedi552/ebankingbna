import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VirementDetailComponent } from './virement-detail.component';

describe('Virement Management Detail Component', () => {
  let comp: VirementDetailComponent;
  let fixture: ComponentFixture<VirementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VirementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./virement-detail.component').then(m => m.VirementDetailComponent),
              resolve: { virement: () => of({ id: 13388 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VirementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VirementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load virement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VirementDetailComponent);

      // THEN
      expect(instance.virement()).toEqual(expect.objectContaining({ id: 13388 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
