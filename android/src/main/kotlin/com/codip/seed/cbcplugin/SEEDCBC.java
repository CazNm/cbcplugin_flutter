package com.codip.seed.cbcplugin;

public class SEEDCBC {
    public static final int SEED_BLOCK_SIZE = 16;

    public static final int ENC = 1;
    public static final int DEC = 0;

    private int 			encrypt;
    private byte[] 			ivec;
    private int[]			seedKey;
    private byte[] 			cbc_buffer;
    private int[] 			buffer_length;
    private byte[] 			cbc_last_block;
    private int[] 			last_block_flag;

    public SEEDCBC() {
        this.ivec = new byte[SEED_BLOCK_SIZE];
        this.seedKey = new int[32];
        this.cbc_buffer = new byte[SEED_BLOCK_SIZE];
        this.buffer_length = new int[1];
        this.cbc_last_block = new byte[SEED_BLOCK_SIZE];
        this.last_block_flag = new int[1];
    }
    static {
        System.loadLibrary("seedcbc");
    }
    protected static native int seedCBCInit(byte[] user_key, int[] seed_key);
    private native int internalSeedCBCProcessEnc(int enc, int[] seedKey, byte[] ivec, byte[] cbc_buffer, int[] buffer_length, byte[] inputText, int inputOffset, int inputTextLen, byte[] outputText, int outputOffset);
    private native int internalSeedCBCProcessDec(int enc, int[] seedKey, byte[] ivec, byte[] cbc_buffer, int[] buffer_length, byte[] cbc_last_block, int[] last_block_flag, byte[] inputText, int inputOffset, int inputTextLen, byte[] outputText, int outputOffset);
    private native int internalSeedProcessBlocks(int[] seedKey, byte[] ivec, byte[] cbc_buffer, byte[] outputText, int outputTextLen);
    protected static native void encryptBlock(byte[] in, byte[] out, int out_index, int[] seed_key);

    public int init(int enc, byte[] key, byte[] iv) {
        if(key == null || iv == null) {
            return 0;
        }

        if(seedCBCInit(key, this.seedKey) == -1) {
            return 0;
        }

        System.arraycopy(iv, 0, this.ivec, 0, SEED_BLOCK_SIZE);
        this.encrypt = enc;
        this.last_block_flag[0] = this.buffer_length[0] = 0;

        return 1;
    }

    public int process(byte[] inputText, int inputOffset, int inputTextLen, byte[] outputText, int outputOffset) {
        if(inputText == null || outputText == null) {
            return 0;
        }

        if(inputTextLen <= 0) {
            return -1;
        }

        if(inputOffset < 0 || inputOffset >= inputText.length || outputOffset < 0 || outputOffset >= outputText.length) {
            return -1;
        }

        int outputTextLen = 0;

        if(this.encrypt == ENC) {
            outputTextLen = internalSeedCBCProcessEnc(this.encrypt, this.seedKey, this.ivec, this.cbc_buffer, buffer_length, inputText, inputOffset, inputTextLen, outputText, outputOffset);

            return outputTextLen;
        } else {
            outputTextLen = internalSeedCBCProcessDec(this.encrypt, this.seedKey, this.ivec, this.cbc_buffer, buffer_length, this.cbc_last_block, last_block_flag, inputText, inputOffset, inputTextLen, outputText, outputOffset);

            return outputTextLen;
        }
    }

    public int close(byte[] outputText, int outputTextLen) {
        if(outputText == null) {
            return 0;
        }

        int i, padLen;

        if(this.encrypt == ENC) {
            padLen = SEED_BLOCK_SIZE - (this.buffer_length[0]);

            for(i = this.buffer_length[0]; i < SEED_BLOCK_SIZE; i++) {
                this.cbc_buffer[i] = (byte)padLen;
            }

            if(internalSeedProcessBlocks(this.seedKey, this.ivec, this.cbc_buffer, outputText, outputTextLen) == -1) {
                return -1;
            }

            outputTextLen = SEED_BLOCK_SIZE;
        } else {
            padLen = SEED_BLOCK_SIZE - this.cbc_last_block[SEED_BLOCK_SIZE - 1];

            if(padLen > SEED_BLOCK_SIZE) {
                return -1;
            }

            if(padLen > 1) {
                i = this.cbc_last_block[SEED_BLOCK_SIZE - 1];

                while(i > 0) {
                    if(this.cbc_last_block[SEED_BLOCK_SIZE - 1] != this.cbc_last_block[SEED_BLOCK_SIZE - i]) {
                        return -1;
                    }

                    i--;
                }
            }

            for(i = 0; i < padLen; i++) {
                outputText[outputTextLen + i] = this.cbc_last_block[i];
            }

            outputTextLen = padLen;
        }

        return outputTextLen;
    }

    public int CBC_ENCRYPT(byte[] user_key, byte[] iv, byte[] inputText, int inputOffset, int inputTextLen, byte[] outputText, int outputOffset) {
        int padLen = 0;
        int outputTextLen = 0;

        if(this.init(ENC, user_key, iv) == 0)
        {
            return 0;
        }

        outputTextLen = this.process(inputText, inputOffset, inputTextLen, outputText, outputOffset);
        if(outputTextLen < 0)
        {
            return 0;
        }

        padLen = this.close(outputText, outputTextLen);
        if(padLen < 0)
        {
            return 0;
        }

        return outputTextLen + padLen;
    }

    public int CBC_DECRYPT(byte[] user_key, byte[] iv, byte[] inputText, int inputOffset, int inputTextLen, byte[] outputText, int outputOffset) {
        int padLen = 0;
        int outputTextLen = 0;

        if(this.init(DEC, user_key, iv) == 0)
        {
            return 0;
        }

        outputTextLen = this.process(inputText, inputOffset, inputTextLen, outputText, outputOffset);
        if(outputTextLen < 0)
        {
            return 0;
        }

        padLen = this.close(outputText, outputTextLen);
        if(padLen < 0)
        {
            return 0;
        }

        return outputTextLen + padLen;
    }

    public int getOutputSize(int inputLen) {
        return this.getOutputSize(this.encrypt, inputLen);
    }

    public int getOutputSize(int enc, int inputLen) {
        int outputLen = 0, padLen;

        if(enc == ENC) {
            padLen = SEED_BLOCK_SIZE - inputLen % SEED_BLOCK_SIZE;
            if(padLen == SEED_BLOCK_SIZE) {
                outputLen = inputLen + SEED_BLOCK_SIZE;
            } else {
                outputLen = inputLen + padLen;
            }
        } else {
            outputLen = inputLen;
        }

        return outputLen ;
    }
}