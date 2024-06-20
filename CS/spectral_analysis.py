import requests as rq
import numpy as np
from scipy.io import wavfile
from scipy.fft import fft
from scipy.signal import butter, sosfilt
from tabulate import tabulate
import IPython.display as ipd
import io

# Buscando o áudio no GitHub (Se online)
# AUDIO_URL = 'https://github.com/RexynyN/current-semester/raw/main/CS/runaway.wav'
# resposta = rq.get(AUDIO_URL) 
# rate, audio = wavfile.read(io.BytesIO(resposta.content))    

# Se for local
AUDIO_PATH = "runaway.wav"
rate, audio = wavfile.read(AUDIO_PATH)    


# Normaliza o áudio entre -1 <= x <= 1
def normalize(x):
    x = x / np.abs(x).max() 
    mask = (x > 1)
    x[mask] = 1
    mask = (x < -1)
    x[mask] = -1
    return x 

# Plot the audio signal in time
import matplotlib.pyplot as plt
plt.plot(audio)
plt.title('Ondas do Som',size=16);
plt.show()

# Calcula o fft e as frequências
N = len(audio)
fft_result = fft(audio)
fft_result = np.abs(fft_result[:N//2])
freqs = np.fft.fftfreq(N, 1/rate)[:N//2]


# Cria o gráfico do espectro
plt.plot(freqs, fft_result)
plt.title('FFT')
plt.xlabel('Frequência (Hz)')
plt.ylabel('Magnitude')
plt.show();


def top_5_frequencies(data, sample_rate):
    # Realiza a Tranformada de Fourier (FFT) e calcula as frequências correspondentes
    N = len(data)
    fft_result = fft(data)
    freqs = np.fft.fftfreq(N, 1/sample_rate)
    
    # Considera apenas a metade positiva do espectro de frequência
    positive_freqs = freqs[:N//2]
    positive_magnitudes = np.abs(fft_result[:N//2])
    
    # Converte as magnitudes para decibéis
    positive_magnitudes_db = 20 * np.log10(positive_magnitudes)
    
    # Retorna as 5 frequências mais presentes e suas magnitudes em dB
    top_indices = np.argsort(positive_magnitudes_db)[-5:][::-1]
    top_frequencies = positive_freqs[top_indices]
    top_magnitudes_db = positive_magnitudes_db[top_indices]
    
    return top_frequencies, top_magnitudes_db

# Pega as 5 frequencias mais intensas
frequencies, magnitudes_db = top_5_frequencies(audio, rate)
table = []
for idx, (freq, mag_db) in enumerate(zip(frequencies, magnitudes_db)):
    table.append([idx+1, f"{freq:.2f} Hz", f"{mag_db:.2f} dB"])

print("Frequências mais intensas:\n")
print(
    tabulate(
        table,
        headers=["Top", "Frequência (Hz)", "Magnitude (dB)"],
    )
)

# Função auxiliar para pegar a faixa de preservação de frequências
def get_cuts(freqs):
    return min(freqs), max(freqs)

# Cria um filtro Butterworth de passa-baixo
def bandpass_filter(data, sample_rate, lowcut, highcut):
    nyq = 0.5 * sample_rate
    low = lowcut / nyq
    high = highcut / nyq

    # Passa o filtro butterworth
    sos = butter(5, [low, high], analog=False, btype='band', output='sos')
    y = sosfilt(sos, data)
    return y.astype(np.int16)

# Pega a faixa de frequências que vamos preservar
lowcut, highcut = get_cuts(frequencies)

# Aplica o filtro passa-baixo na faixa decidida e salva o áudio
filtered_data = bandpass_filter(audio, rate, lowcut, highcut)

clean_filename = AUDIO_PATH.split(".")[0]
wavfile.write(f'{clean_filename}-filtered.wav', rate, filtered_data)

print(f"Faixa de frequência filtrada: {lowcut:.2f} Hz - {highcut:.2f} Hz")
