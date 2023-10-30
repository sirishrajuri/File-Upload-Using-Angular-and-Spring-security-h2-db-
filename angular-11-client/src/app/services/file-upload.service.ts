import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(files: File[]): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    files.forEach(file => {
      formData.append('files', file, file.name);
    });
    const req = new HttpRequest('POST', `http://localhost:8080/fileApi/upload`, formData, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${this.baseUrl}/fileApi/files`);
  }
}