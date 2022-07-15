import { RequestStatusBadgeComponent } from './request-status-badge.component';
import { DocumentRequestStatus } from '../common/types';

describe('RequestStatusBadgeComponent', () => {
  let component: RequestStatusBadgeComponent;

  beforeEach(() => {
    component = new RequestStatusBadgeComponent();
  });

  it('should use "info" color if status uncnown', () => {
    component.status = 'foo-bar' as any;
    expect(component.color).toEqual('info');
  });

  it('should set "success" on RequestStatus.Open', () => {
    component.status = DocumentRequestStatus.Open;
    expect(component.color).toEqual('success');
  });

  it('should set "warning" on RequestStatus.Pending', () => {
    component.status = DocumentRequestStatus.Pending;
    expect(component.color).toEqual('warning');
  });

  it('should set "warning" on RequestStatus.Review', () => {
    component.status = DocumentRequestStatus.Pending;
    expect(component.color).toEqual('warning');
  });

  it('should set "success" on RequestStatus.Accepted', () => {
    component.status = DocumentRequestStatus.Approved;
    expect(component.color).toEqual('success');
  });

  it('should set "danger" on RequestStatus.Rejected', () => {
    component.status = DocumentRequestStatus.Rejected;
    expect(component.color).toEqual('danger');
  });

  it('should set "danger" on RequestStatus.Cancelled', () => {
    component.status = DocumentRequestStatus.Cancelled;
    expect(component.color).toEqual('danger');
  });
});
