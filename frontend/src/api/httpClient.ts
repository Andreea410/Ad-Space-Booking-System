const API_BASE_URL = process.env.REACT_APP_API_BASE_URL ?? '';

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const text = await response.text().catch(() => '');
    throw new Error(text || `Request failed with status ${response.status}`);
  }
  return (await response.json()) as T;
}

export async function getJson<T>(path: string, init?: RequestInit): Promise<T> {
  const url = `${API_BASE_URL}${path}`;
  const response = await fetch(url, {
    ...init,
    method: init?.method ?? 'GET',
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers ?? {}),
    },
  });
  return handleResponse<T>(response);
}

export async function postJson<TRequest, TResponse>(
  path: string,
  body: TRequest,
  init?: RequestInit,
): Promise<TResponse> {
  const url = `${API_BASE_URL}${path}`;
  const response = await fetch(url, {
    ...init,
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers ?? {}),
    },
    body: JSON.stringify(body),
  });
  return handleResponse<TResponse>(response);
}


