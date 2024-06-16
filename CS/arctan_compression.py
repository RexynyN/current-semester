import numpy as np
from scipy.interpolate import interp1d
from scipy.io import wavfile

# Função de utilidade para aplicar a transformação 
def apply(signal, transfer, interpolation='linear'):
    constant = np.linspace(-1, 1, len(transfer))
    interpolator = interp1d(constant, transfer, interpolation)
    return interpolator(signal)

# Compressor de amplitude do áudio (usando arco-tangente)
def arctan_compressor(x, factor=2):
    constant = np.linspace(-1, 1, 1000)
    transfer = np.arctan(factor * constant)
    transfer /= np.abs(transfer).max()
    transfer = assert_range(transfer)
    return apply(x, transfer)

# Faz um check para valores maiores que os limites por causa da 
# imprecisão de floats ex: 1.00000084729
def assert_range(x):
    mask = (x > 1)
    x[mask] = 1
    mask = (x < -1)
    x[mask] = -1
    return x 

# Orquestração das transformações
def transform_audio(filename, factor=2):
    sr, x = wavfile.read(filename)
    # Deixa ele entre -1 <= x <= 1
    x = x / np.abs(x).max() 
    x = assert_range(x)

    comp = arctan_compressor(x, factor)
    comp = np.int16(comp * 32767)

    new_file = filename.split(".")[0]
    new_file += "-tranformed.wav"
    wavfile.write(new_file, sr, comp)

transform_audio("audio.wav", 5)