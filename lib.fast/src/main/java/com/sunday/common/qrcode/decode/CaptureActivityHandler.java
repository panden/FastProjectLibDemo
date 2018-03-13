package com.sunday.common.qrcode.decode;

import android.os.Handler;
import android.os.Message;

import com.sunday.common.R;
import com.sunday.common.qrcode.CaptureActivity;
import com.sunday.common.qrcode.camera.CameraManager;

public final class CaptureActivityHandler extends Handler {

	DecodeThread decodeThread = null;
	CaptureActivity activity = null;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}
	public static final int auto_focus = 0;
	public static final int decode = 1;
	public static final int decode_failed = 2;
	public static final int decode_succeeded = 3;
	public static final int restart_preview = 4;
	public static final int quit = 5;
	public static final int return_scan_result = 6;


	public CaptureActivityHandler(CaptureActivity activity) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity);
		decodeThread.start();
		state = State.SUCCESS;
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {

		switch (message.what) {
		case auto_focus:
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, CaptureActivityHandler.auto_focus);
			}
			break;
		case restart_preview:
			restartPreviewAndDecode();
			break;
		case decode_succeeded:
			state = State.SUCCESS;
			activity.handleDecode((String) message.obj);// 解析成功，回调
			break;

		case decode_failed:
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					CaptureActivityHandler.decode);
			break;
		}

	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		removeMessages(CaptureActivityHandler.decode_succeeded);
		removeMessages(CaptureActivityHandler.decode_failed);
		removeMessages(CaptureActivityHandler.decode);
		removeMessages(CaptureActivityHandler.auto_focus);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					CaptureActivityHandler.decode);
			CameraManager.get().requestAutoFocus(this, CaptureActivityHandler.auto_focus);
		}
	}

}
