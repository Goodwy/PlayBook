package com.goodwy.audiobook.uitools

import android.animation.Animator
import android.animation.AnimatorSet
import android.transition.TransitionValues
import android.transition.Visibility
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.goodwy.audiobook.uitools.noPauseAnimator.noPause

private const val FINAL_RADIUS = "com.goodwy.audiobook:CircularRevealBookPlayTransition:finalRadius"

class CircularRevealBookPlayTransition : Visibility() {

  override fun onAppear(
    sceneRoot: ViewGroup?,
    startValues: TransitionValues?,
    startVisibility: Int,
    endValues: TransitionValues?,
    endVisibility: Int
  ): Animator? {
    if (endVisibility != View.VISIBLE || endValues == null)
      return null

    val view = endValues.view
    val parent = view.parent as? View?
        ?: return null
    val parentWidth = parent.width
    val finalRadius = endValues.values.getOrElse(FINAL_RADIUS) {
      parentWidth
    } as Int

    val circularReveal = circularRevealAnimator(
      target = endValues.view,
      cx = parentWidth / 2,
      finalRadius = finalRadius.toFloat()
    )
      .apply {
        interpolator = Interpolators.fastOutSlowIn
        addListener(object : DefaultAnimatorListener {
          override fun onAnimationStart(animator: Animator) {
            view.visibility = endVisibility
          }
        })
      }

    return AnimatorSet()
      .apply {
        playTogether(circularReveal)
        view.visibility = View.INVISIBLE
      }
  }

  private fun circularRevealAnimator(target: View, cx: Int, finalRadius: Float): Animator =
    ViewAnimationUtils.createCircularReveal(
      target,
      cx - target.left,
      target.height / 2,
      0f,
      finalRadius
    ).noPause()
}
